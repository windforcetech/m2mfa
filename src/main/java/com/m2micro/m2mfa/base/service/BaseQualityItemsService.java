package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseQualityItemsQuery;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsAddDetails;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsModel;

/**
 * 检验项目 服务类
 * @author liaotao
 * @since 2019-01-28
 */
public interface BaseQualityItemsService extends BaseService<BaseQualityItems,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseQualityItems> list(BaseQualityItemsQuery query);

    /**
     * 检验项目新增需要的数据
     * @return 检验项目新增需要的数据
     */
    BaseQualityItemsAddDetails addDetails();

    /**
     * 保存
     * @param baseQualityItems
     * @return
     */
    BaseQualityItems saveEntity(BaseQualityItems baseQualityItems);

    /**
     * 详情
     * @param id
     * @return
     */
    BaseQualityItemsModel info(String id);

    /**
     * 保存
     * @param baseQualityItems
     * @return
     */
    BaseQualityItems updateEntity(BaseQualityItems baseQualityItems);

    /**
     * 删除
     * @param ids
     */
    void deleteEntitys(String[] ids);
}