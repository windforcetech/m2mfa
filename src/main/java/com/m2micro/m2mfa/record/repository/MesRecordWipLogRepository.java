package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 在制记录表历史 Repository 接口
 * @author liaotao
 * @since 2019-03-27
 */
@Repository
public interface MesRecordWipLogRepository extends BaseRepository<MesRecordWipLog,String> {

    @Query("select sum(inputQty) from MesRecordWipLog where scheduleId=?1 and wipNextProcess=?2")
    Integer getAllInputQty(String scheduleId,String wipNextProcess);

    @Query("select sum(outputQty) from MesRecordWipLog where scheduleId=?1 and wipNowProcess=?2")
    Integer getAllOutputQty(String scheduleId,String processId);

    @Query("select sum(outputQty) from MesRecordWipLog where scheduleId=?1 and wipNowProcess=?2  and staffId=?3 and outTime>=?4")
    Integer getActualOutput(String scheduleId,String processId,String staffId, Date outTime);
}
