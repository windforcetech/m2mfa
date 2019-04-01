package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 不良输入记录 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesRecordFailRepository extends BaseRepository<MesRecordFail,String> {
  List<MesRecordFail>findByDefectCode(String defectCode);

  /**
   * 根据排产单id和工序id获取不良记录
   * @param scheduleId
   * @param processId
   * @return
   */
  @Query(value = "SELECT mrf.* FROM mes_record_fail mrf, mes_record_work mrw WHERE mrf.rw_id=mrw.rwid AND mrf.repair_flag <>1 AND mrw.schedule_id=?1 AND mrw.process_id=?2",nativeQuery = true)
  List<MesRecordFail> getByProcessIdAndScheduleId(String scheduleId,String processId);

}
