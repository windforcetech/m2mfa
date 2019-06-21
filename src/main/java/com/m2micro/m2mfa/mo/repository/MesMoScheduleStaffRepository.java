package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 生产排程人员 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesMoScheduleStaffRepository extends BaseRepository<MesMoScheduleStaff,String> {
    /**
     * 设置排程人员的实际结束时间
     * @param actualEndTime
     *          实际结束时间
     * @param scheduleId
     *          排产单id
     * @return     影响行数
     */
    @Modifying
    @Query("update MesMoScheduleStaff m set m.actualEndTime = ?1 where m.scheduleId = ?2 and m.actualStartTime is not null and m.actualEndTime is null")
    Integer setEndAll(Date actualEndTime, String scheduleId);

    /**
     * 根据排产单编号删除数据
     * @param scheduleId
     */
    @Modifying
    @Query(value = "DELETE FROM   MesMoScheduleStaff  mmss WHERE  mmss.scheduleId=?1")
    void deleteScheduleId(String scheduleId);

    /**
     *  获取排程人员
     * @param scheduleId
     *          排产单id
     * @return  排程人员
     */
    List<MesMoScheduleStaff> findByScheduleId(String scheduleId);

    /**
     * 根据排产单ID跟人员ID获取排产单人员数据
     * @param scheduleId
     * @param staffId
     * @return
     */
    @Query(value = "select * from mes_mo_schedule_staff mmss where mmss.staff_id =?2 and mmss.schedule_id=?1",nativeQuery = true)
    List<MesMoScheduleStaff> findByScheduleIdandStafftId(String scheduleId,String staffId);

    /**
     * 根据排产单id和工位id获取排产单人员数据
     * @param scheduleId
     * @param stationId
     * @return
     */
    List<MesMoScheduleStaff> findByScheduleIdAndStationId(String scheduleId, String stationId);

    /**
     * 获取开机工位人员信息
     * @param scheduleId
     *          排产单id
     * @param code
     *          开机工位所在code
     * @return
     */
    @Query(value = "SELECT\n" +
                    "	ms.* \n" +
                    "FROM\n" +
                    "	mes_mo_schedule_staff ms,\n" +
                    "	base_station bs \n" +
                    "WHERE\n" +
                    "	ms.station_id = bs.station_id \n" +
                    "	AND ms.schedule_id = ?1\n" +
                    "	AND bs.code = ?2",nativeQuery = true)
    MesMoScheduleStaff getMesMoScheduleStaffForBoot(String scheduleId,String code);

    /**
     * 设置工序员工的结束时间
     * @param actualEndTime
     *          结束时间
     * @param scheduleId
     *          排产单
     * @param processId
     *          工序
     * @return  影响行数
     */
    @Modifying
    @Query("update MesMoScheduleStaff m set m.actualEndTime = ?1 where m.scheduleId = ?2 and m.processId=?3 and m.actualStartTime is not null and m.actualEndTime is null")
    Integer setEndTimeForProcess(Date actualEndTime, String scheduleId,String processId);
}