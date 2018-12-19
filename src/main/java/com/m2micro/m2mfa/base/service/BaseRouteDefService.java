package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 生产途程单身 服务类
 * @author chenshuhong
 * @since 2018-12-17
 */
public interface BaseRouteDefService extends BaseService<BaseRouteDef,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseRouteDef> list(Query query);

    //根据process查找
    String selectoneprocessId(String processId);
    //根据routeId删除
    void deleterouteId(String routeId);

}