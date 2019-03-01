package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordMold;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * 上模记录表 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesRecordMoldRepository extends BaseRepository<MesRecordMold,String> {
  /**
   * 根据rid获取对应的上工模具
   * @param rwId
   * @return
   */
  @Query(value = "select * from mes_record_mold where rw_id=?1 ",nativeQuery = true)
  MesRecordMold findRwId(String rwId);

}
