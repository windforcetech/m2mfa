package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工位基本档 Repository 接口
 * @author liaotao
 * @since 2018-11-30
 */
@Repository
public interface BaseStationRepository extends BaseRepository<BaseStation,String> {

    List<BaseStation> findByCodeAndStationIdNot(String code,String stationId);

}