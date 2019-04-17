package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 不良現象代碼 Repository 接口
 * @author liaotao
 * @since 2019-03-05
 */
@Repository
public interface BaseDefectRepository extends BaseRepository<BaseDefect,String> {

  List<BaseDefect>findByEctCode(String ectCode);

  List<BaseDefect> findByEctCodeAndEctIdNot(String ectCode,String ectId);

  List<BaseDefect> findByEctNameAndEctIdNot(String ectCode,String ectId);
}
