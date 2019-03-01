package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验方案主档 Repository 接口
 * @author liaotao
 * @since 2019-01-28
 */
@Repository
public interface BaseQualitySolutionDescRepository extends BaseRepository<BaseQualitySolutionDesc,String> {

  /**
   * 根据抽样ID获取检验（）
   * @param aqlId
   * @return
   */
  List<BaseQualitySolutionDesc> findByAqlId(String aqlId);

  @Query(value = "SELECT mmd.output_process_id FROM mes_mo_desc mmd, mes_mo_schedule mms WHERE mmd.mo_id = mms.mo_id AND mms.schedule_id = ?1", nativeQuery = true)
  String getOutputProcessId(String scheduleId);
}
