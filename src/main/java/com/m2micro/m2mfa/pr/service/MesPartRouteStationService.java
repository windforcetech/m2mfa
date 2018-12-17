package com.m2micro.m2mfa.pr.service;

import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 料件途程设定工位 服务类
 * @author chenshuhong
 * @since 2018-12-17
 */
public interface MesPartRouteStationService extends BaseService<MesPartRouteStation,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesPartRouteStation> list(Query query);
}