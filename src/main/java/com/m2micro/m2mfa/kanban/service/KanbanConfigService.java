package com.m2micro.m2mfa.kanban.service;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;

public interface KanbanConfigService {
  /**
   * 添加模板配置
   * @param baseLedConfig
   * @return
   */
  void save( BaseLedConfig baseLedConfig);

  /**
   * 删除
   * @param ids
   */
  void deleteByIds(String [] ids);

  /**
   * 获取配置项的详情
   * @param id
   * @return
   */
  BaseLedConfig  findById(String id);

  /**
   * 获取配置列表
   * @param query
   * @return
   */
  PageUtil<BaseLedConfig> list(Query query);
}
