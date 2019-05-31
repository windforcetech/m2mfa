package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.vo.Yield;

import java.util.List;

public interface YieldService {

  /**
   * 产量报表
   * @return
   */
  List<Yield> YieldShow(YieldQuery yieldQuery);

  /**
   * excel导出生产报表
   * @param yieldQuery
   */
  void excelOutData(YieldQuery yieldQuery)throws Exception;
}
