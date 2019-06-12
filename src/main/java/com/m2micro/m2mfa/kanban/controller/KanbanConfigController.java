package com.m2micro.m2mfa.kanban.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.m2mfa.kanban.service.KanbanConfigService;
import com.m2micro.m2mfa.push.MyChatServerHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kanban/kanbanConfig")
@Api(value="看板配置项")
@Authorize
public class KanbanConfigController {

    @Autowired
    KanbanConfigService kanbanConfigService;

  /**
   * 保存
   */
  @PostMapping("/save")
  @ApiOperation(value="保存")
  @UserOperationLog("保存")
  public ResponseMessage<BaseLedConfig> save(@RequestBody BaseLedConfig baseLedConfig){
    kanbanConfigService.save(baseLedConfig);
    MyChatServerHandler.pushMainScreen();
    return ResponseMessage.ok();
  }


  /**
   * 删除
   */
  @PostMapping("/delete")
  @ApiOperation(value="删除")
  @UserOperationLog("删除")
  public ResponseMessage delete(@RequestBody String[] ids){
    kanbanConfigService.deleteByIds(ids);
    return ResponseMessage.ok();
  }


  /**
   * 详情
   */
  @PostMapping("/info/{id}")
  @ApiOperation(value="详情")
  @UserOperationLog("详情")
  public ResponseMessage<BaseLedConfig> info(@PathVariable("id") String id){
    BaseLedConfig baseLedConfig = kanbanConfigService.findById(id);
    return ResponseMessage.ok(baseLedConfig);
  }


  /**
   * 列表
   */
  @PostMapping("/list")
  @ApiOperation(value="列表")
  @UserOperationLog("列表")
  public ResponseMessage<PageUtil<BaseLedConfig>> list(Query query){
    PageUtil<BaseLedConfig> page = kanbanConfigService.list(query);
   
    return ResponseMessage.ok(page);
  }


  /**
   * 更新
   */
  @PostMapping("/update")
  @ApiOperation(value="更新")
  @UserOperationLog("更新")
  public ResponseMessage<BaseLedConfig> update(@RequestBody BaseLedConfig baseLedConfig){
    ValidatorUtil.validateEntity(baseLedConfig, UpdateGroup.class);
    BaseLedConfig baseLedConfigOld = kanbanConfigService.findById(baseLedConfig.getConfigId());
    if(baseLedConfigOld==null){
      throw new MMException("数据库不存在该记录");
    }
    String [] ids=new String[]{baseLedConfig.getConfigId()};
    kanbanConfigService.deleteByIds(ids);
    kanbanConfigService.renew(baseLedConfig);
    MyChatServerHandler.pushMainScreen();
    return ResponseMessage.ok();
  }


}
