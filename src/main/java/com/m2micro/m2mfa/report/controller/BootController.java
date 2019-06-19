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
  @PostMapping("/BootShow")
  @ApiOperation(value="开机日报报表显示")
  @UserOperationLog("开机日报报表显示")
  public ResponseMessage<BootAndData> BootShow(BootQuery bootQuery){

    return  ResponseMessage.ok(bootService.BootShow(bootQuery));
  }


}
