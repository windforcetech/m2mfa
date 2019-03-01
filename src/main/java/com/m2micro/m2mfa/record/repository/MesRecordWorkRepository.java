package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 上工记录表 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesRecordWorkRepository extends BaseRepository<MesRecordWork,String> {
    @Query(value="SELECT mrw.* FROM mes_record_work mrw WHERE mrw.schedule_id <> ?1 AND mrw.machine_id = ?2 AND mrw.end_time IS NOT NULL ORDER BY mrw.end_time DESC, mrw.rwid DESC LIMIT 1",nativeQuery = true)
    MesRecordWork getOldMesRecordWork(String scheduleId,String machineId);

    @Query(value = "SELECT  mrw.rwid  FROM mes_record_work mrw WHERE mrw.schedule_id = ?1 AND mrw.station_id =?2 AND mrw.start_time IS NOT NULL AND ISNULL(mrw.end_time)", nativeQuery = true)
    String  isStationisWork(String scheduleId, String stationId) ;

    @Query(value = "select * from mes_record_work  where schedule_id=?1 and station_id=?2 and start_time IS NOT NULL  ORDER BY  start_time  desc  LIMIT 0,1   " ,nativeQuery = true)
    MesRecordWork selectMesRecordWork (String scheduleId, String stationId) ;

    /**
     * 根据排产单id和工位id获取上工记录
     * @param scheduleId
     * @param stationId
     * @return
     */
    List<MesRecordWork> findByScheduleIdAndStationIdAndStartTimeNotNull(String scheduleId, String stationId);

    /**
     * 根据排产单ids和工位id获取上工记录
     * @param scheduleIds
     * @param stationId
     * @return
     */
    List<MesRecordWork> findByScheduleIdInAndStationId(List<String> scheduleIds,String stationId);

    /**
     * 设置上下工记录结束时间，电量，模数
     * @param endTime
     *          结束时间
     * @param endPower
     *          电量
     * @param endMolds
     *          模数
     * @param scheduleId
     *          排产单id
     * @return     影响行数
     */
    @Modifying
    @Query("update MesRecordWork m set m.endTime = ?1 , m.endPower = ?2 , m.endMolds = ?3 where m.scheduleId = ?4 and m.startTime is not null and m.endTime is null")
    Integer setEndAll(Date endTime, BigDecimal endPower, BigDecimal endMolds, String scheduleId);
}
