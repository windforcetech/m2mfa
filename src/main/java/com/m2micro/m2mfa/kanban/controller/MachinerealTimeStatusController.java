package com.m2micro.m2mfa.kanban.controller;

import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.kanban.service.MachinerealTimeStatusService;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseMessage<List<MachinerealTimeData>> MachinerealTimeStatusShow(HttpServletRequest request){

    System.out.println("ip+"+getRemortIP(request));
    return  ResponseMessage.ok(machinerealTimeStatusService.MachinerealTimeStatusShow(getRemortIP(request)));
  }


  /**
   * 获取IP地址
   *
   * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public String getRemortIP(HttpServletRequest request) {


      String ip = null;
      try {
        ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();
        }
      } catch (Exception e) {

      }
      return ip;


  }


}
