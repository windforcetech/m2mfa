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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

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

  ReentrantLock lock = new ReentrantLock();
  /**
   * 列表
   */
  @RequestMapping("/erpBasebomdesc")
  @ApiOperation(value="料件物料清单erp")
  @UserOperationLog("料件物料清单erp")
  public ResponseMessage erpBasebomdesc(String partNo,String  distinguish ){
    lock.lock();
    Long aLong = baseBomDescErpService.erpBasebomdescCount(partNo,distinguish);
    Long chun = chun(aLong);
    for(long i=0;i<chun;i++){
      baseBomDescErpService.erpBasebomdesc(partNo,distinguish,i,1000L);
    }
    baseBomDescErpService.erpBasebomdesc(partNo,distinguish,(chun),(aLong-(chun*1000)));

    lock.unlock();
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
