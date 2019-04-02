package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseAlert;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 预警方式设定 服务类
 * @author chenshuhong
 * @since 2019-04-02
 */
public interface BaseAlertService extends BaseService<BaseAlert,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseAlert> list(Query query);
}