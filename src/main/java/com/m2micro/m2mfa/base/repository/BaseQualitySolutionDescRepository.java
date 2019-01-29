package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import com.m2micro.framework.commons.BaseRepository;
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
}
