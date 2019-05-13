package com.m2micro.m2mfa.erp.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.erp.service.BaseStaffErpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员erp 前端控制器
 * @author chenshuhong
 * @since 2019/3/28
 */
@RestController
@RequestMapping("/erp/basestaff")
@Api(value="人员 erp")
@Authorize
public class BaseStaffErpContoller {

  @Autowired
  private BaseStaffErpService baseStaffErpService;
  /**
   * 列表
   */
  @RequestMapping("/erpBasestaff")
  @ApiOperation(value="人员erp")
  @UserOperationLog("人员erp")
  public ResponseMessage erpBasestaff(String code ){
    Long aLong = baseStaffErpService.erpBasestaffCount(code);
    Long chun = chun(aLong);
    for(long i=0;i<chun;i++){
      baseStaffErpService.erpBasestaff(code,i,1000L);
    }
    baseStaffErpService.erpBasestaff(code,(chun),(aLong-(chun*1000)));
    return ResponseMessage.ok();
  }


  public  static  Long chun(Long countlong){
    Long num=1l;
    if(countlong>1000){
      num= countlong/ 1000 ;
    }
    return num;
  }

}
