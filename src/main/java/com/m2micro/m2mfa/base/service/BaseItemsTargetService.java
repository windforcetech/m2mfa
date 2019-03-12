package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;

import java.util.List;

/**
 * 参考资料对应表 服务类
 * @author liaotao
 * @since 2018-11-30
 */
public interface BaseItemsTargetService extends BaseService<BaseItemsTarget,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseItemsTarget> list(Query query);

    /**
     * 根据资料维护主表名称获取所有参考资料
     * @param itemName
     *          资料维护主表名称
     * @return  资料对应表的所有详情
     */
    List<BaseItemsTarget> getAllItemsTarget(String itemName);

    /**
     * 根据资料维护主表名称获取所有参考资料下拉选项
     * @param itemCode
     *          资料维护主表名称
     * @return  资料对应表的所有下拉选项
     */
    List<SelectNode> getSelectNode(String itemCode);
    /**
     * 根据资料维护主表名称获取所有参考资料树形选项
     * @param itemCode
     *          资料维护主表名称
     * @return  资料对应表的所有树形选项
     */
    TreeNode getTreeNode(String itemCode);

    /**
     * 保存
     * @param baseItemsTarget
     * @return
     */
    BaseItemsTarget saveEntity(BaseItemsTarget baseItemsTarget);

    /**
     * 修改
     * @param baseItemsTarget
     * @return
     */
    BaseItemsTarget updateEntity(BaseItemsTarget baseItemsTarget);


}