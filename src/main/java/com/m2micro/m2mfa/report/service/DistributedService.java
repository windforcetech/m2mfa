package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.query.DistributedQuery;
import com.m2micro.m2mfa.report.vo.Distributed;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DistributedService {
  /**
   * 在线制表
   * @param distributedQuery
   * @return
   */
  List<Distributed>  DistributedShow(DistributedQuery distributedQuery);


  /**
   * 导出exlcex数据
   * @param distributedQuery
   * @param response
   * @throws Exception
   */
  void  excelOutData(DistributedQuery distributedQuery,  HttpServletResponse response)throws Exception;

  /**
   * pdf数据导出
   * @param distributedQuery
   * @param response
   */
  void  pdfOutData(DistributedQuery distributedQuery,  HttpServletResponse response)throws Exception ;
}
