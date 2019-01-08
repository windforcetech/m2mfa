package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

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
    @Query(value = "DELETE FROM mes_mo_schedule_staff WHERE  schedule_id=?1",nativeQuery = true)
    void deleteScheduleId(String scheduleId);

}