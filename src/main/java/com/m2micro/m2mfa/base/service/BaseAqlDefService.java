package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 抽样标准(aql)-明细 服务类
 * @author liaotao
 * @since 2019-01-29
 */
public interface BaseAqlDefService extends BaseService<BaseAqlDef,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseAqlDef> list(Query query);
}