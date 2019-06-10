package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.vo.Distributed;

import java.util.List;

public interface BootService {
  /**
   * 开机日报报表显示
   * @param bootQuery
   * @return
   */
  List<Distributed> BootShow(BootQuery bootQuery);
}
