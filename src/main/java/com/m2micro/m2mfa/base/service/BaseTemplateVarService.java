package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 模板变量 服务类
 * @author liaotao
 * @since 2019-01-22
 */
public interface BaseTemplateVarService extends BaseService<BaseTemplateVar,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseTemplateVar> list(Query query);
}