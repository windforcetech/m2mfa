package com.m2micro.m2mfa.kanban.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.common.util.GetAndIputil;
import com.m2micro.m2mfa.kanban.service.MachinerealTimeStatusService;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
  @PostMapping("/MachinerealTimeStatusShow")
  @ApiOperation(value="机台实时数据显示")
  @UserOperationLog("机台实时数据显示")
  public ResponseMessage<List<MachinerealTimeData>> MachinerealTimeStatusShow( String elemen,HttpServletRequest request){
    if(elemen==null){
      elemen = GetAndIputil.getIpAddr(request);
      System.out.println("进来获取"+elemen);
    }

    return  ResponseMessage.ok(machinerealTimeStatusService.MachinerealTimeStatusShow(elemen));
  }


}
