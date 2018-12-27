package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 上工记录表 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesRecordWorkRepository extends BaseRepository<MesRecordWork,String> {
    @Query(value="SELECT mrw.* FROM mes_record_work mrw WHERE mrw.schedule_id <> ?1 AND mrw.machine_id = ?2 AND mrw.end_time IS NOT NULL ORDER BY mrw.end_time DESC, mrw.rwid DESC LIMIT 1",nativeQuery = true)
    MesRecordWork getOldMesRecordWork(String scheduleId,String machineId);
}