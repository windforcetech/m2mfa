package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.vo.PostProcessAndData;
import com.m2micro.m2mfa.report.vo.PostProcessQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机台生成状况
 */
@RestController
@RequestMapping("/report/MachineProduce")
@Api(value="机台生成状况",description="机台生成状况")
public class MachineProduceContoller {
  /**
   * 后工序日报报表
   */
  @RequestMapping("/MachineProduceShow")
  @ApiOperation(value="机台生成状况报表显示")
  @UserOperationLog("机台生成状况报表显示")
  public ResponseMessage MachineProduceShow() {

    return  ResponseMessage.ok();
  }

}
