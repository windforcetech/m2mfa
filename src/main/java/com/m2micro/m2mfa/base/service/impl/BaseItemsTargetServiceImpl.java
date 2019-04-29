package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.m2mfa.base.entity.BaseItems;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.repository.BaseInstructionRepository;
import com.m2micro.m2mfa.base.repository.BaseItemsRepository;
import com.m2micro.m2mfa.base.repository.BaseItemsTargetRepository;
import com.m2micro.m2mfa.base.service.BaseItemsService;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseItemsTarget;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 参考资料对应表 服务实现类
 *
 * @author liaotao
 * @since 2018-11-30
 */
@Service
public class BaseItemsTargetServiceImpl implements BaseItemsTargetService {
    @Autowired
    BaseItemsTargetRepository baseItemsTargetRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    BaseItemsRepository baseItemsRepository;
    @Autowired
    BaseInstructionRepository baseInstructionRepository;

    public BaseItemsTargetRepository getRepository() {
        return baseItemsTargetRepository;
    }

    @Override
    public PageUtil<BaseItemsTarget> list(Query query) {
        QBaseItemsTarget qBaseItemsTarget = QBaseItemsTarget.baseItemsTarget;
        JPAQuery<BaseItemsTarget> jq = queryFactory.selectFrom(qBaseItemsTarget);

        jq.offset((query.getPage() - 1) * query.getSize()).limit(query.getSize());
        List<BaseItemsTarget> list = jq.fetch();
        long totalCount = jq.fetchCount();
        return PageUtil.of(list, totalCount, query.getSize(), query.getPage());
    }

    @Override
    public List<BaseItemsTarget> getAllItemsTarget(String itemName) {
        // 获取资料主表
        List<BaseItems> items = baseItemsRepository.findAllByItemName(itemName);
        // 如果不存在及不唯一（校验）
        if (items == null || items.size() != 1) {
            throw new MMException("资料主档不存在或异常");
        }
        // 树形
        BaseItems baseItems = items.get(0);
        if (baseItems.getTree()) {
            return getAllBaseItemsTarget(baseItems.getItemId());
        }
        // 不是树形
        return baseItemsTargetRepository.findAllByItemId(baseItems.getItemId());
    }


    /**
     * 获取资料主档对应的所有子节点
     *
     * @param itemId
     * @return
     */
    private List<BaseItemsTarget> getAllBaseItemsTarget(String itemId) {
        List<BaseItemsTarget> list = new ArrayList<>();
        //获取主档对应的子档资料
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByItemId(itemId);
        if (baseItemsTargets == null || (baseItemsTargets != null && baseItemsTargets.size() == 0)) {
            throw new MMException("资料主档对应的资料信息不存在！");
        }
        list.addAll(baseItemsTargets);
        for (BaseItemsTarget baseItemsTarget : baseItemsTargets) {
            List<BaseItemsTarget> allChildren = getAllChildren(baseItemsTarget.getId());
            if (allChildren != null && allChildren.size() > 0) {
                list.addAll(allChildren);
            }
        }
        return list;
    }

    /**
     * 获取所有资料的子节点
     *
     * @param id
     * @return
     */
    private List<BaseItemsTarget> getAllChildren(String id) {
        List<BaseItemsTarget> list = new ArrayList<>();
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByTreeParentId(id);
        if (baseItemsTargets == null) {
            return null;
        }
        list.addAll(baseItemsTargets);
        for (BaseItemsTarget baseItemsTarget : baseItemsTargets) {
            List<BaseItemsTarget> lt = getAllChildren(baseItemsTarget.getId());
            if (lt != null) {
                list.addAll(lt);
            }
        }
        return list;
    }

    @Override
    public List<SelectNode> getSelectNode(String itemCode) {
        // 获取资料主表
        List<BaseItems> items = baseItemsRepository.findAllByItemCode(itemCode);
        // 如果不存在及不唯一（校验）
        if (items == null || items.size() != 1) {
            throw new MMException("资料主档不存在或异常");
        }
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByItemId(items.get(0).getItemId());
        List<SelectNode> list = new ArrayList<>();
        baseItemsTargets.stream().forEach(baseItemsTarget -> {
            list.add(new SelectNode(baseItemsTarget.getId(), baseItemsTarget.getItemName(), baseItemsTarget.getItemValue()));
        });
        return list;
    }

    @Override
    public TreeNode getTreeNode(String itemCode) {
        // 获取资料主表
        List<BaseItems> items = baseItemsRepository.findAllByItemCode(itemCode);
        // 如果不存在及不唯一（校验）
        if (items == null || items.size() != 1) {
            throw new MMException("资料主档不存在或异常");
        }
        return getAllTreeNode(items.get(0));
    }

    @Override
    @Transactional
    public BaseItemsTarget saveEntity(BaseItemsTarget baseItemsTarget) {
        ValidatorUtil.validateEntity(baseItemsTarget, AddGroup.class);

        BaseItemsTarget baseItemsTargetParent = baseItemsTargetRepository.findById(baseItemsTarget.getTreeParentId()).orElse(null);
        baseItemsTarget.setId(UUIDUtil.getUUID());
        baseItemsTarget.setItemId(baseItemsTargetParent.getItemId());
        return save(baseItemsTarget);
    }

    @Override
    @Transactional
    public BaseItemsTarget updateEntity(BaseItemsTarget baseItemsTarget) {
        ValidatorUtil.validateEntity(baseItemsTarget, UpdateGroup.class);
        BaseItemsTarget baseItemsTargetOld = findById(baseItemsTarget.getId()).orElse(null);
        if (baseItemsTargetOld == null) {
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseItemsTarget, baseItemsTargetOld);
        return save(baseItemsTargetOld);
    }

    @Override
    public ResponseMessage delete(String[] ids) {
        ResponseMessage responseMessage = ResponseMessage.ok();
        List<BaseItemsTarget> ls = new ArrayList<>();
        for (String id : ids) {
            List<BaseInstruction> byCategory = baseInstructionRepository.findByCategory(id);
            BaseItemsTarget baseItemsTarget = findById(id).orElse(null);
            if (byCategory.isEmpty()) {
                deleteById(id);
            } else {
                ls.add(baseItemsTarget);
            }

        }

        if (!ls.isEmpty()) {
            String[] strings = ls.stream().map(BaseItemsTarget::getItemName).toArray(String[]::new);
            responseMessage.setMessage("类型【" + String.join(",", strings) + "】被作业指导书引用");
        }
        return responseMessage;
    }

    /**
     * 获取所有资料的子节点
     *
     * @param item
     * @return
     */
    private TreeNode getAllTreeNode(BaseItems item) {
        TreeNode root = new TreeNode(item.getItemId(), item.getItemName());
        //获取主档对应的子档资料
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByItemId(item.getItemId());
        if (baseItemsTargets == null || (baseItemsTargets != null && baseItemsTargets.size() == 0)) {
            throw new MMException("资料主档对应的资料信息不存在！");
        }
        List<TreeNode> firstAllTreeNode = getFirstAllTreeNode(baseItemsTargets);
        for (TreeNode treeNode : firstAllTreeNode) {
            getAllChildrenTreeNode(treeNode, baseItemsTargets);
            root.getChildren().add(treeNode);
        }
        return root;
    }

    private void getAllChildrenTreeNode(TreeNode treeNode, List<BaseItemsTarget> baseItemsTargets) {
        //获取下一级所有节点
        List<TreeNode> nextAllTreeNode = getNextAllTreeNode(treeNode, baseItemsTargets);
        if (nextAllTreeNode == null || (nextAllTreeNode != null && nextAllTreeNode.size() == 0)) {
            return;
        }
        for (TreeNode nextTreeNode : nextAllTreeNode) {
            getAllChildrenTreeNode(nextTreeNode, baseItemsTargets);
            treeNode.getChildren().add(nextTreeNode);
        }
    }

    private List<TreeNode> getFirstAllTreeNode(List<BaseItemsTarget> baseItemsTargets) {
        List<TreeNode> list = new ArrayList<>();
        List<BaseItemsTarget> exclude = new ArrayList<>();
        for (BaseItemsTarget baseItemsTarget : baseItemsTargets) {
            if (baseItemsTarget.getId().equalsIgnoreCase(baseItemsTarget.getTreeParentId())) {
                list.add(new TreeNode(baseItemsTarget.getId(), baseItemsTarget.getItemName()));
                exclude.add(baseItemsTarget);
            }
        }
        baseItemsTargets.removeAll(exclude);
        return list;
    }


    private List<TreeNode> getNextAllTreeNode(TreeNode treeNode, List<BaseItemsTarget> baseItemsTargets) {
        List<TreeNode> list = new ArrayList<>();
        for (BaseItemsTarget baseItemsTarget : baseItemsTargets) {
            if (treeNode.getId().equals(baseItemsTarget.getTreeParentId())) {
                list.add(new TreeNode(baseItemsTarget.getId(), baseItemsTarget.getItemName()));
            }
        }
        return list;
    }

}
