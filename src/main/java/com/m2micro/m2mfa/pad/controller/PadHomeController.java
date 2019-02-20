package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.pad.service.PadHomeService;
import com.m2micro.m2mfa.pad.model.PadHomeModel;
import com.m2micro.m2mfa.pad.model.PadHomePara;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主页显示信息 前端控制器
 * @author chenshuhong
 * @since 2019-01-24
 */
@RestController
@RequestMapping("/pad/home")
@Api(value="主页显示信息 前端控制器")
@Authorize(Authorize.authorizeType.AllowAll)
public class PadHomeController {
@Autowired
private PadHomeService padHomeService;
  /**
   * 个人显示列表
   */
  @RequestMapping("/findByHome")
  @ApiOperation(value="个人显示列表")
  @UserOperationLog("个人显示列表")
  public ResponseMessage<PadHomeModel> findByHome(@RequestBody PadHomePara padHomePara){
    return ResponseMessage.ok( padHomeService.findByHome(padHomePara));
  }



}
