package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.vo.PostProcessAndData;
import com.m2micro.m2mfa.report.vo.PostProcessQuery;

public interface PostProcessService {
  /**
   * 后工序数据显示
   * @param postProcessQuery
   * @return
   */
  PostProcessAndData PostProcessShow(PostProcessQuery postProcessQuery);
}
