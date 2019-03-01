package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * 不良現象代碼 Repository 接口
 * @author chenshuhong
 * @since 2019-01-24
 */
@Repository
public interface BaseDefectRepository extends BaseRepository<BaseDefect,String> {

  @Query(value = "select distinct bd.ect_code from base_defect bd  LEFT JOIN  mes_record_fail mrf  on bd.ect_code=mrf.defect_code where bd.ect_code=?1",nativeQuery = true)
  String  findEctCode(String ectCode);

  /**
   * 根据不良Id获取数据
   * @param ectId
   * @return
   */
 BaseDefect findByEctId(String ectId);

}
