package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 不良原因代码 服务类
 * @author liaotao
 * @since 2019-01-28
 */
public interface BaseSymptomService extends BaseService<BaseSymptom,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseSymptom> list(Query query);
}