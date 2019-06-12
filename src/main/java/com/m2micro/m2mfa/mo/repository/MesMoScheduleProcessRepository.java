package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
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

    /**
     * 获取当前排产单的模具id
     * @param scheduleId
     * @param processId
     * @return
     */
    @Query(value = "SELECT distinct mmsp.mold_id from mes_mo_schedule_process mmsp WHERE mmsp.schedule_id=?1 and mmsp.process_id=?2",nativeQuery = true)
    String getProductionMoldId(String scheduleId,String processId);

    /**
     * 根据排产单及工序id获取生产排程工序信息
     * @param scheduleIds
     * @param processId
     * @return
     */
    List<MesMoScheduleProcess> findByScheduleIdInAndProcessId(List<String> scheduleIds,String processId);

    /**
     * 获取排程工序
     * @param scheduleId
     * @return
     */
    List<MesMoScheduleProcess> findByScheduleIdAndMoldIdNotNull(String scheduleId);

    /**
     * 判断模具是否被引用
     * @param moldId
     * @return
     */
    List<MesMoScheduleProcess> findByMoldIdAndGroupId(String moldId,String groupId);


    /**
     * 获取没结束的工序
     * @param scheduleId
     * @param processId
     * @return
     */
    List<MesMoScheduleProcess> findByScheduleIdAndProcessIdAndActualEndTimeIsNotNull(String scheduleId,String processId);

    /**
     * 获取生产排程工序
     * @param scheduleId
     * @param processId
     * @return
     */
    MesMoScheduleProcess findByScheduleIdAndProcessId(String scheduleId,String processId);


    @Modifying
    @Query(value = "update mes_mo_schedule_process set output_qty = ?1, beer_qty = ?2 where id = ?3",nativeQuery = true)
    Integer updateOutputQtyAndMold(Integer outputQty,Integer beerQty,String id);
}
