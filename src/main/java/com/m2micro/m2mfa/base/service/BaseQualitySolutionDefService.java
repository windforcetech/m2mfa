package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 检验方案明细 服务类
 * @author liaotao
 * @since 2019-01-28
 */
public interface BaseQualitySolutionDefService extends BaseService<BaseQualitySolutionDef,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseQualitySolutionDef> list(Query query);
}