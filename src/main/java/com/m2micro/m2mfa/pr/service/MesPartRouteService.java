package com.m2micro.m2mfa.pr.service;

import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 料件途程设定主档 服务类
 * @author liaotao
 * @since 2018-12-19
 */
public interface MesPartRouteService extends BaseService<MesPartRoute,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesPartRoute> list(Query query);

    //根据工艺id查询业务
     String selectRouteid(String routeId);
}