package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BasePartQualitySolution;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 料件品质方案关联 服务类
 * @author liaotao
 * @since 2019-03-05
 */
public interface BasePartQualitySolutionService extends BaseService<BasePartQualitySolution,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BasePartQualitySolution> list(Query query);
}