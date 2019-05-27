package com.m2micro.m2mfa.kanban.repository;

import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  Repository 接口
 * @author chenshuhong
 * @since 2019-05-27
 */
@Repository
public interface BaseLedConfigRepository extends BaseRepository<BaseLedConfig,String> {

  void deleteByConfigIdIn(String[] ids);

}
