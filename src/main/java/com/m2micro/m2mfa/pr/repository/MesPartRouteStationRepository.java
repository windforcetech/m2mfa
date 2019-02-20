package com.m2micro.m2mfa.pr.repository;

import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 料件途程设定工位 Repository 接口
 * @author chenshuhong
 * @since 2018-12-17
 */
@Repository
public interface MesPartRouteStationRepository extends BaseRepository<MesPartRouteStation,String> {

    List<MesPartRouteStation> findByStationIdIn(List stationIds);
    @Transactional
    @Modifying
    @Query(value = "delete from mes_part_route_station where  part_route_id=?1",nativeQuery = true)
    void deletemesParRouteID(String partrouteid);

    /**
     * 获取料件途程设定工位
     * @param partRouteId
     * @return
     */
    List<MesPartRouteStation> findByPartRouteId(String partRouteId);

    /**
     * 获取该工序对应的途程
     * @param partRouteId
     * @param processId
     * @param stationId
     * @return
     */
    List<MesPartRouteStation> findByPartRouteIdAndProcessIdAndStationId(String partRouteId,String processId,String stationId);
}
