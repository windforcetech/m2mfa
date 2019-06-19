package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.vo.BootAndData;

import javax.servlet.http.HttpServletResponse;

public interface BootService {
  /**
   * 开机日报报表显示
   * @param bootQuery
   * @return
   */
   BootAndData BootShow(BootQuery bootQuery);

  /**
   * excel导出生产报表
   * @param bootQuery
   */
  void excelOutData(BootQuery bootQuery, HttpServletResponse response)throws Exception;

  /**
   * pdf导出生产报表
   * @param bootQuery
   */
  void pdfOutData(BootQuery bootQuery,  HttpServletResponse response)throws Exception;
}
