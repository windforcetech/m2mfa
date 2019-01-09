package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleShift;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
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
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM MesMoScheduleShift  mmss WHERE  mmss.scheduleId=?1")
    void deleteScheduleId(String scheduleId);
}