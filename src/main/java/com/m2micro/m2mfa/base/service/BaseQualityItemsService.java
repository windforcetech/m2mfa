package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
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
    PageUtil<BaseQualityItems> list(Query query);
}