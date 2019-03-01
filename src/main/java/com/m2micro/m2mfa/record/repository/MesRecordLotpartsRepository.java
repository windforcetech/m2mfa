package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordLotparts;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 加料记录表 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesRecordLotpartsRepository extends BaseRepository<MesRecordLotparts,String> {
  /**
   * 根据Rwid获取加料记录
   * @param rwId
   * @return
   */
  List<MesRecordLotparts > findByRwId(String rwId);
}
