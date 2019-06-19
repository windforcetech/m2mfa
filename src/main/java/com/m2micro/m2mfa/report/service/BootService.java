package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.vo.BootAndData;

import java.util.List;

public interface BootService {
  /**
   * 开机日报报表显示
   * @param bootQuery
   * @return
   */
   BootAndData BootShow(BootQuery bootQuery);
}
