package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleShift;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *  Repository 接口
 * @author liaotao
 * @since 2019-01-04
 */
@Repository
public interface MesMoScheduleShiftRepository extends BaseRepository<MesMoScheduleShift,String> {
    /**
     * 根据排产单编号删除数据
     * @param scheduleId
     */
    @Modifying
    @Query(value = "DELETE FROM mes_mo_schedule_shift WHERE  schedule_id=?1",nativeQuery = true)
    void deleteScheduleId(String scheduleId);
}