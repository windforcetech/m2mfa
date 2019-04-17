package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM  MesMoScheduleStation mms WHERE  mms.scheduleId=?1")
    void deleteScheduleId(String scheduleId);

    /**
     *  获取排程工位
     * @param scheduleId
     *          排产单id
     * @return  排程工位
     */
    List<MesMoScheduleStation> findByScheduleId(String scheduleId);

    /**
     * 获取排程工位
     * @param scheduleId
     * @param stationId
     * @return
     */
    MesMoScheduleStation findByScheduleIdAndStationId(String scheduleId,String stationId);
}