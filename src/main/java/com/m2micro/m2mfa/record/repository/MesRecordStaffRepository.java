package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 人员作业记录 Repository 接口
 * @author wanglei
 * @since 2018-12-27
 */
@Repository
public interface MesRecordStaffRepository extends BaseRepository<MesRecordStaff,String> {
    /**
     * 设置人员作业记录结束时间，电量，模数
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
    @Query("update MesRecordStaff m set m.endTime = ?1 , m.endPower = ?2 , m.endMolds = ?3 where m.scheduleId = ?4 and m.startTime is not null and m.endTime is null")
    Integer setEndAll(Date endTime, BigDecimal endPower,BigDecimal endMolds, String scheduleId);

    /**
     * 根据员工id获取对应的记录
     * @param staffId
     * @return
     */

    @Query(value = "SELECT  * from  mes_record_staff  where  staff_id=?1",nativeQuery = true)
    List<MesRecordStaff> findStaffId(String staffId);

    /**
     * 查找人员上工记录
     * @param rwId
     * @return
     */
    MesRecordStaff findByRwIdAndStartTimeNotNullAndEndTimeIsNull(String rwId);


}
