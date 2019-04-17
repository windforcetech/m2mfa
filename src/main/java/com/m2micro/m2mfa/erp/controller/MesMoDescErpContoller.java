package com.m2micro.m2mfa.erp.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.erp.service.MesMoDescErpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单erp 前端控制器
 * @author chenshuhong
 * @since 2019/3/28
 */
@RestController
@RequestMapping("/erp/mesMoDesc")
@Api(value="工单erp")
@Authorize
public class MesMoDescErpContoller {

  @Autowired
  private MesMoDescErpService mesMoDescErpService;
  /**
   * 列表
   */
  @RequestMapping("/erpMesMoDesc")
  @ApiOperation(value="工单erp")
  @UserOperationLog("工单erp")
  public ResponseMessage erpMesMoDesc(String moNumber ){
    return   mesMoDescErpService.erpMesMoDesc(moNumber);
  }


}
