package com.m2micro.m2mfa.pr.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.query.MesPartRouteQuery;
import com.m2micro.m2mfa.pr.vo.MesPartvo;

import java.util.List;

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
    PageUtil<MesPartRoute> list(MesPartRouteQuery query);

    //根据工艺id查询业务
    List<String> selectRouteid(String routeId);

     //添加途程
     boolean save( MesPartRoute mesPartRoute,List<MesPartRouteProcess >mesPartRouteProcesss, List<MesPartRouteStation> mesPartRouteStations);

     boolean update( MesPartRoute mesPartRoute,List<MesPartRouteProcess >mesPartRouteProcesss, List<MesPartRouteStation> mesPartRouteStations);

     MesPartvo info(String partRouteId);

     String  delete(String id);
}