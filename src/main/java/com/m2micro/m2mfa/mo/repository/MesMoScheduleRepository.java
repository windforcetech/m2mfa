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
import java.util.List;

/**
 * 生产排程表表头 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesMoScheduleRepository extends BaseRepository<MesMoSchedule,String> {
    @Query(value="select SUM(mprs.standard_hours) from mes_mo_desc mmd,mes_part_route_station mprs where mmd.route_id=mprs.part_route_id and mmd.mo_id=?1",nativeQuery=true)
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

}