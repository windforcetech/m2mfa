package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordFail;
import com.m2micro.framework.commons.BaseRepository;
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
}
