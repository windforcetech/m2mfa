package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 抽样标准(aql)-明细 Repository 接口
 * @author liaotao
 * @since 2019-01-29
 */
@Repository
public interface BaseAqlDefRepository extends BaseRepository<BaseAqlDef,String> {
  /**
   * 根据抽样id删除明细
   * @param aqlId
   */
  @Transactional
  void deleteByAqlId(String aqlId);

  /**
   * 根据aqlid获取抽样详情
   * @param aqlId
   * @return
   */
  List<BaseAqlDef> findByAqlId(String aqlId);

}
