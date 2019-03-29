package com.m2micro.m2mfa.erp.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.erp.service.BaseBomDescErpService;
import com.m2micro.m2mfa.erp.service.BasePartsErpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件物料清单erp 前端控制器
 * @author chenshuhong
 * @since 2019/3/28
 */
@RestController
@RequestMapping("/erp/basebomdesc")
@Api(value="料件物料清单erp")
@Authorize
public class BaseBomDescErpContoller {

  @Autowired
  private BaseBomDescErpService baseBomDescErpService;

  /**
   * 列表
   */
  @RequestMapping("/erpBasebomdesc")
  @ApiOperation(value="料件物料清单erp")
  @UserOperationLog("料件物料清单erp")
  public ResponseMessage erpBasebomdesc(String partNo,String  distinguish ){
    baseBomDescErpService.erpBasebomdesc(partNo,distinguish);
    return ResponseMessage.ok();
  }

}
