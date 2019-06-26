package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.report.query.MachineProduceQuery;
import com.m2micro.m2mfa.report.service.MachineProduceService;
import com.m2micro.m2mfa.report.vo.MachineProduce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机台生产状况
 */
@RestController
@RequestMapping("/report/MachineProduce")
@Api(value="机台生产状况",description="机台生产状况")
public class MachineProduceContoller {
  @Autowired
  MachineProduceService machineProduceService;


  /**
   * 后工序日报报表
   */
  @RequestMapping("/MachineProduceShow")
  @ApiOperation(value="机台生产状况报表显示")
  @UserOperationLog("机台生产状况报表显示")
  public ResponseMessage<List<MachineProduce>>  MachineProduceShow(MachineProduceQuery machineProduceQuery) {
    return  ResponseMessage.ok(machineProduceService.MachineProduceShow(machineProduceQuery));
  }

}
