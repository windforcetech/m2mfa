package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.BootAndData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 开机日报
 */
@RestController
@RequestMapping("/report/Boot")
@Api(value="开机日报",description="开机日报")
public class BootController {

  @Autowired
  private BootService bootService;
  /**
   * 开机日报报表
   */
  @RequestMapping("/BootShow")
  @ApiOperation(value="开机日报报表显示")
  @UserOperationLog("开机日报报表显示")
  public ResponseMessage<BootAndData> BootShow(BootQuery bootQuery){

    return  ResponseMessage.ok(bootService.BootShow(bootQuery));
  }

  /**
   * exce导出生产产量报表
   */
  @RequestMapping("/excelOutData")
  @ApiOperation(value="exce导出生产产量报表")
  @UserOperationLog("exce导出生产产量报表")
  public void  excelOutData(BootQuery bootQuery,  HttpServletResponse response)throws Exception{
    bootService.excelOutData(bootQuery ,response);

  }

  /**
   * pdf导出生产产量报表
   */
  @RequestMapping("/pdfOutData")
  @ApiOperation(value="pdf导出生产产量报表")
  @UserOperationLog("pdf导出生产产量报表")
  public void  pdfOutData(BootQuery bootQuery,  HttpServletResponse response)throws Exception{
    bootService.pdfOutData(bootQuery ,response);

  }
}
