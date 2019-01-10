package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 生产排程工序 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesMoScheduleProcessRepository extends BaseRepository<MesMoScheduleProcess,String> {
    /**
     * 设置排程工序的实际结束时间
     * @param actualEndTime
     *          实际结束时间
     * @param scheduleId
     *          排产单id
     * @return     影响行数
     */
    @Modifying
    @Query("update MesMoScheduleProcess m set m.actualEndTime = ?1 where m.scheduleId = ?2 and m.actualStartTime is not null and m.actualEndTime is null")
    Integer setEndAll(Date actualEndTime, String scheduleId);

    /**
     * 根据排产单编号删除数据
     * @param scheduleId
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM  MesMoScheduleProcess mmp WHERE  mmp.scheduleId=?1")
    void deleteScheduleId(String scheduleId);


    /**
     * 获取对应工序
     * @param scheduleId
     * @param processId
     * @return
     */

    @Query(value = "select * from mes_mo_schedule_process where schedule_id =?1 and process_id=?2 " ,nativeQuery = true)
    MesMoScheduleProcess findbscheduleIdProcessId(String scheduleId, String processId);

    /**
     *  获取排程工序
     * @param scheduleId
     *          排产单id
     * @return  排程工序
     */
    List<MesMoScheduleProcess> findByScheduleId(String scheduleId);
}