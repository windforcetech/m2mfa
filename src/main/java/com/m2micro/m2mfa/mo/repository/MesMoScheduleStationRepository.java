package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * 生产排程工位 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesMoScheduleStationRepository extends BaseRepository<MesMoScheduleStation,String> {
    /**
     * 根据排产单编号删除数据
     * @param scheduleId
     */
    @Modifying
    @Query(value = "DELETE FROM mes_mo_schedule_station WHERE  schedule_id=?1",nativeQuery = true)
    void deleteScheduleId(String scheduleId);
}