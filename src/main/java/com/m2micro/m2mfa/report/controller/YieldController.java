package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.query.YieldDataQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 生产产量报表
 */
@RestController
@RequestMapping("/report/Yield")
@Api(value="生产产量报表",description ="生产产量报表" )
public class YieldController {

  @Autowired
  private YieldService  yieldService ;

  /**
   * 生产产量报表
   */
  @RequestMapping("/YieldShow")
  @ApiOperation(value="生产产量报表显示")
  @UserOperationLog("生产产量报表显示")
  public ResponseMessage<List<Yield>> YieldShow(YieldQuery yieldQuery){

    return  ResponseMessage.ok(yieldService.yieldShow(yieldQuery));
  }


  /**
   * 生产产量报表分页
   */
  @RequestMapping("/Yielddata")
  @ApiOperation(value="生产产量报表显示")
  @UserOperationLog("生产产量报表显示")
  public ResponseMessage<PageUtil<Yield>> Yielddata(YieldDataQuery yieldQuery){

    return  ResponseMessage.ok(yieldService.yielddata(yieldQuery));
  }



  /**
   * exce导出生产产量报表
   */
  @RequestMapping("/excelOutData")
  @ApiOperation(value="exce导出生产产量报表")
  @UserOperationLog("exce导出生产产量报表")
  public ResponseMessage excelOutData(YieldQuery yieldQuery,  HttpServletResponse response)throws Exception{
    yieldService.excelOutData(yieldQuery ,response);
    return  ResponseMessage.ok();
  }

  /**
   * pdf导出生产产量报表
   */
  @RequestMapping("/pdfOutData")
  @ApiOperation(value="pdf导出生产产量报表")
  @UserOperationLog("pdf导出生产产量报表")
  public ResponseMessage pdfOutData(YieldQuery yieldQuery,  HttpServletResponse response)throws Exception{
    yieldService.pdfOutData(yieldQuery ,response);
    return  ResponseMessage.ok();
  }


}
