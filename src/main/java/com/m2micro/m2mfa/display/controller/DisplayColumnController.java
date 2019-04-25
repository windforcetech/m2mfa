package com.m2micro.m2mfa.display.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.display.service.DisplayColumnService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.display.entity.DisplayColumn;
import org.springframework.web.bind.annotation.RestController;

/**
 * 显示列 前端控制器
 * @author liaotao
 * @since 2019-04-25
 */
@RestController
@RequestMapping("/display/displayColumn")
@Api(value="显示列 前端控制器",description = "显示列")
@Authorize
public class DisplayColumnController {
    @Autowired
    DisplayColumnService displayColumnService;

    /**
     * 列表
     */
    /*@RequestMapping("/list")
    @ApiOperation(value="显示列列表")
    @UserOperationLog("显示列列表")
    public ResponseMessage<PageUtil<DisplayColumn>> list(Query query){
        PageUtil<DisplayColumn> page = displayColumnService.list(query);
        return ResponseMessage.ok(page);
    }*/

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="显示列详情")
    @UserOperationLog("显示列详情")
    public ResponseMessage<DisplayColumn> info(String moduleId){
        return ResponseMessage.ok(displayColumnService.info(moduleId));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value="保存显示列")
    @UserOperationLog("保存显示列")
    public ResponseMessage<DisplayColumn> save(@RequestBody DisplayColumn displayColumn){
        return ResponseMessage.ok(displayColumnService.saveEntity(displayColumn));
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value="更新显示列")
    @UserOperationLog("更新显示列")
    public ResponseMessage<DisplayColumn> update(@RequestBody DisplayColumn displayColumn){
        return ResponseMessage.ok(displayColumnService.updateEntity(displayColumn));
    }

    /**
     * 删除
     */
    /*@RequestMapping("/delete")
    @ApiOperation(value="删除显示列")
    @UserOperationLog("删除显示列")
    public ResponseMessage delete(@RequestBody String[] ids){
        displayColumnService.deleteByIds(ids);
        return ResponseMessage.ok();
    }*/

}