package com.m2micro.m2mfa.erp.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.erp.service.BasePartsErpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件erp 前端控制器
 * @author chenshuhong
 * @since 2019/3/28
 */
@RestController
@RequestMapping("/erp/parts")
@Api(value="料件erp")
@Authorize
public class BasePartsErpContoller {

 @Autowired
  private BasePartsErpService basePartsErpService;
  /**
   * 列表
   */
  @RequestMapping("/erpParts")
  @ApiOperation(value="料件erp")
  @UserOperationLog("料件erp")
  public ResponseMessage erpParts(String partNos){
      Long aLong = basePartsErpService.erpPartsCount(partNos);
      Long chun = chun(aLong);
      for(long i=0;i<chun;i++){
          basePartsErpService.erpParts(partNos,i,1000L);
      }
      basePartsErpService.erpParts(partNos,(chun),(aLong-(chun*1000)));

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
