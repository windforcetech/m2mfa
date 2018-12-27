package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseAbnormal;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 异常项目档 服务类
 * @author liaotao
 * @since 2018-12-27
 */
public interface BaseAbnormalService extends BaseService<BaseAbnormal,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseAbnormal> list(Query query);
}