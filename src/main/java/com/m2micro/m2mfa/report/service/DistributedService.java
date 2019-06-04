package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.DistributedQuery;
import com.m2micro.m2mfa.report.vo.Distributed;

import java.util.List;

public interface DistributedService {
  /**
   * 在线制表
   * @param distributedQuery
   * @return
   */
  List<Distributed>  DistributedShow(DistributedQuery distributedQuery);
}
