package com.m2micro.m2mfa.kanban.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.common.util.GetAndIputil;
import com.m2micro.m2mfa.kanban.service.MesMoDescTimeDataService;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTime;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTimeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/kanban/MesMoDescTimeData")
@Api(value="工单实时数据")
public class MesMoDescTimeDataController {
  @Autowired
  MesMoDescTimeDataService mesMoDescTimeDataService;


  /**
   * 机台实时数据显示
   */
  @PostMapping("/MesMoDescTimeDataShow")
  @ApiOperation(value="工单实时数据显示")
  @UserOperationLog("工单实时数据显示")
  public ResponseMessage<MesMoDescTime> MesMoDescTimeDataShow( String elemen, HttpServletRequest request){
    if(elemen==null){

      elemen = GetAndIputil.getIpAddr(request);
      System.out.println("进来获取"+elemen);
    }

    return ResponseMessage.ok(mesMoDescTimeDataService.MesMoDescTimeDataShow(elemen));
  }
}
