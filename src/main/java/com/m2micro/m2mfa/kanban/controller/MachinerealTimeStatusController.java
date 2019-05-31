package com.m2micro.m2mfa.kanban.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.kanban.service.MachinerealTimeStatusService;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kanban/machinerealTimeStatus")
@Api(value="看板配置项")
public class MachinerealTimeStatusController {


  @Autowired
  MachinerealTimeStatusService machinerealTimeStatusService;
  /**
   * 机台实时数据显示
   */
  @PostMapping("/MachinerealTimeStatusShow/{elemen}")
  @ApiOperation(value="机台实时数据显示")
  @UserOperationLog("机台实时数据显示")
  public ResponseMessage<List<MachinerealTimeData>> MachinerealTimeStatusShow(@PathVariable("elemen")  String  elemen){

    return  ResponseMessage.ok(machinerealTimeStatusService.MachinerealTimeStatusShow(elemen));
  }

}
