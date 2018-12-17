package com.m2micro.m2mfa.pr.repository;

import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件途程设定工位 Repository 接口
 * @author chenshuhong
 * @since 2018-12-17
 */
@Repository
public interface MesPartRouteStationRepository extends BaseRepository<MesPartRouteStation,String> {

    List<MesPartRouteStation> findByStationIdIn(List stationIds);
}