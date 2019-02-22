package com.m2micro.m2mfa.mo.repository;

import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.m2micro.m2mfa.mo.constant.MoScheduleStatus.AUDITED;

/**
 * 生产排程表表头 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesMoScheduleRepository extends BaseRepository<MesMoSchedule,String> {
    @Query(value="select  ifnull( SUM(mprs.standard_hours),0)  from mes_mo_desc mmd,mes_part_route_station mprs where mmd.route_id=mprs.part_route_id and mmd.mo_id=?1",nativeQuery=true)
    BigDecimal getScheduleTime(String moId);

    /**
     * 更新排产单状态
     * @param flag
     *          状态
     * @param scheduleId
     *          排产单号
     * @return  影响的数据条数
     */
    @Modifying
    @Query("update MesMoSchedule m set m.flag = ?1 where m.scheduleId = ?2")
    Integer setFlagFor(Integer flag, String scheduleId);

    @Modifying
    @Query("update MesMoSchedule m set m.actualEndTime = ?1 where m.scheduleId = ?2")
    void updateactualStartTime(Date actualEndTime,String scheduleId);

    /**
     * 更新工单状态及冻结前状态
     * @param flag
     *          状态
     * @param prefreezingState
     *          冻结前状态
     * @param scheduleId
     *          工单id
     * @return     影响的数据条数
     */
    @Modifying
    @Query("update MesMoSchedule m set m.flag = ?1 , m.prefreezingState = ?2 where m.scheduleId = ?3")
    Integer setFlagAndPrefreezingStateFor(Integer flag,Integer prefreezingState, String scheduleId);


    /**
     * 通过机台id查找排产单数量
     * @param machineId
     *          机台id
     * @return
     */
    Integer countByMachineId(String machineId);

    /**
     * 获取排产编号，如果为空表示数据库一次也没有生成
     * @param moId
     * @return
     */
    @Query(value = "SELECT LPAD(max(IFNULL(SUBSTRING(schedule_no ,-2),'0'))+1, 2, 0) FROM mes_mo_schedule where mo_id=?1",nativeQuery = true)
    String getScheduleNoByMoId(String moId);

    /**
     * 根据排产单id查找排产单
     * @param scheduleIds
     *          排产单id
     * @return  排产单
     */
    List<MesMoSchedule> findByScheduleIdIn(List<String> scheduleIds);

    /**
     * 更新机台id
     * @param machineId
     *          机台id
     * @param scheduleId
     *          排产单id
     * @return  影响行数
     */
    @Modifying
    @Query("update MesMoSchedule m set m.machineId = ?1 where m.scheduleId = ?2")
    Integer updateMachineIdByScheduleId(String machineId,String scheduleId);

    /**
     * 获取当前机器正在生产的排产单
     * @param machineId
     *              机台id
     * @return
     */
    @Query(value = "SELECT mms.*  from mes_mo_schedule mms WHERE mms.flag =?2 and mms.machine_id=?1",nativeQuery = true)
    List<MesMoSchedule> getProductionMesMoScheduleByMachineId(String machineId,Integer flag);

    /**
     * 获取当前机台优先级最高的排产单
     * @param machineId
     *          机台id
     * @param flag
     *          排产单状态
     * @return 排产单
     */
    @Query(value = "SELECT mms.* FROM mes_mo_schedule mms WHERE mms.machine_id = ?1 AND mms.flag = ?2 ORDER BY sequence ASC, create_on ASC LIMIT 1",nativeQuery = true)
    MesMoSchedule getFirstMesMoScheduleByMachineId(String machineId,Integer flag);

    /**
     * 根据工单查找排产单
     * @param moId
     *          工单id
     * @return
     */
    List<MesMoSchedule> findByMoId(String moId);
}
