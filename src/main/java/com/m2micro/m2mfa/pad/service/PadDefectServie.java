package com.m2micro.m2mfa.pad.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseDefect;

public interface PadDefectServie extends BaseService<BaseDefect,String> {
  /**
   * 分页查询
   * @param query
   *         查询参数
   * @return  分页信息
   */
  PageUtil<BaseDefect> list(Query query);
}
