package com.m2micro.m2mfa.base.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseItems;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.repository.BaseItemsRepository;
import com.m2micro.m2mfa.base.repository.BaseItemsTargetRepository;
import com.m2micro.m2mfa.base.service.BaseItemsService;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.QBaseItemsTarget;

import java.util.ArrayList;
import java.util.List;
/**
 * 参考资料对应表 服务实现类
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
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    public List<BaseItemsTarget> getAllItemsTarget(String itemName) {
        // 获取资料主表
        List<BaseItems> items = baseItemsRepository.findAllByItemName(itemName);
        // 如果不存在及不唯一（校验）
        if(items==null||items.size()!=1){
            throw new MMException("资料主档不存在或异常");
        }
        // 树形
        BaseItems baseItems = items.get(0);
        if(baseItems.getTree()){
            return getAllBaseItemsTarget(baseItems.getItemId());
        }
        // 不是树形
        return baseItemsTargetRepository.findAllByItemId(baseItems.getItemId());
    }

    /**
     * 获取资料主档对应的所有子节点
     * @param itemId
     * @return
     */
    private List<BaseItemsTarget> getAllBaseItemsTarget(String itemId){
        List<BaseItemsTarget> list = new ArrayList<>();
        //获取主档对应的子档资料
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByItemId(itemId);
        if(baseItemsTargets==null||(baseItemsTargets!=null&&baseItemsTargets.size()==0)){
            throw new MMException("资料主档对应的资料信息不存在！");
        }
        list.addAll(baseItemsTargets);
        for (BaseItemsTarget baseItemsTarget:baseItemsTargets){
            List<BaseItemsTarget> allChildren = getAllChildren(baseItemsTarget.getId());
            if(allChildren!=null&&allChildren.size()>0){
                list.addAll(allChildren);
            }
        }
        return list;
    }

    /**
     * 获取所有资料的子节点
     * @param id
     * @return
     */
    private List<BaseItemsTarget> getAllChildren(String id){
        List<BaseItemsTarget> list = new ArrayList<>();
        List<BaseItemsTarget> baseItemsTargets = baseItemsTargetRepository.findAllByTreeParentId(id);
        if(baseItemsTargets==null){
            return null;
        }
        list.addAll(baseItemsTargets);
        for(BaseItemsTarget baseItemsTarget:baseItemsTargets){
            List<BaseItemsTarget> lt = getAllChildren(baseItemsTarget.getId());
            if(lt!=null){
                list.addAll(lt);
            }
        }
        return list;
    }

}