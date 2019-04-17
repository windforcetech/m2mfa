package com.m2micro.m2mfa.pr.repository;

import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.framework.commons.BaseRepository;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件途程设定主档 Repository 接口
 * @author liaotao
 * @since 2018-12-19
 */
@Repository
public interface MesPartRouteRepository extends BaseRepository<MesPartRoute,String> {
    @Query("select r.partRouteId  from  MesPartRoute  as r  where r.routeId =?1 ")
    List<String> selectRouteid(String routeId);

    /**
     * 查找涂程
     * @param partId
     *          料件id
     * @return 涂程
     */
    List<MesPartRoute> findByPartId(String partId);

    @Query("select r.partRouteId  from  MesPartRoute  as r  where r.routeId =?1  and  r.partId=?2")
    String  is_experience( String routeId,String partId );


    @Query(value = "SELECT\n" +
            "	mpr.part_route_id\n" +
            "FROM\n" +
            "	mes_mo_schedule mms,\n" +
            "	mes_mo_desc mmd,\n" +
            "	mes_part_route mpr \n" +
            "WHERE mms.mo_id = mmd.mo_id\n" +
            "AND mmd.part_id = mpr.part_id\n" +
            "AND mms.schedule_id=?1",nativeQuery = true)
    String getPartRouteId(String partRouteId);
}