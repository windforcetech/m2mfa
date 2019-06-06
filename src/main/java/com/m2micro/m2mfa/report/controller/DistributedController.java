package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.query.DistributedQuery;
import com.m2micro.m2mfa.report.service.DistributedService;
import com.m2micro.m2mfa.report.vo.Distributed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在制分布报表
 *
 */
@RestController
@RequestMapping("/report/Distributed")
@Api(value="在制分布报表")

public class DistributedController {

  @Autowired
  DistributedService distributedService;

  /**
   * 在制分布报表
   */
  @PostMapping("/DistributedShow")
  @ApiOperation(value="在制分布报表显示")
  @UserOperationLog("在制分布报表显示")
  public ResponseMessage<List<Distributed>> DistributedShow(DistributedQuery  distributedQuery){

    return  ResponseMessage.ok(distributedService.DistributedShow(distributedQuery));
  }


  /**
   * exce导出在制分布报表
   */
  @PostMapping("/excelOutData")
  @ApiOperation(value="exce导出在制分布报表")
  @UserOperationLog("exce导出在制分布报表")
  public ResponseMessage excelOutData(DistributedQuery distributedQuery,  HttpServletResponse response)throws Exception{
    distributedService.excelOutData(distributedQuery,response);
    return  ResponseMessage.ok();
  }


  /**
   * pdf导出生产产量报表
   */
  @PostMapping("/pdfOutData")
  @ApiOperation(value="pdf导出在制分布报表")
  @UserOperationLog("pdf导出在制分布报表")
  public ResponseMessage pdfOutData(DistributedQuery distributedQuery,  HttpServletResponse response)throws Exception{
    distributedService.pdfOutData(distributedQuery ,response);
    return  ResponseMessage.ok();
  }
}
