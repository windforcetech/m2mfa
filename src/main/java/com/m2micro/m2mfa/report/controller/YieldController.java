package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 生产产量报表
 */
@RestController
@RequestMapping("/report/Yield")
@Api(value="看板配置项")
public class YieldController {

  @Autowired
  private YieldService  yieldService ;

  /**
   * 生产产量报表
   */
  @PostMapping("/YieldShow")
  @ApiOperation(value="生产产量报表显示")
  @UserOperationLog("生产产量报表显示")
  public ResponseMessage<List<Yield>> YieldShow(YieldQuery yieldQuery){

    return  ResponseMessage.ok(yieldService.YieldShow(yieldQuery));
  }
}
