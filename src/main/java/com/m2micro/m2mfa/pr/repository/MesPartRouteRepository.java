package com.m2micro.m2mfa.pr.repository;

import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.framework.commons.BaseRepository;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * 料件途程设定主档 Repository 接口
 * @author liaotao
 * @since 2018-12-19
 */
@Repository
public interface MesPartRouteRepository extends BaseRepository<MesPartRoute,String> {
    @Query("select r.partRouteId  from  MesPartRoute  as r  where r.routeId =?1 ")
    String selectRouteid(String routeId);
}