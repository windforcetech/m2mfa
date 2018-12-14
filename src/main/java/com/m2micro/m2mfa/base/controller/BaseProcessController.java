package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.service.BaseProcessService;
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
import com.m2micro.m2mfa.base.entity.BaseProcess;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工序基本档 前端控制器
 * @author chenshuhong
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/base/baseProcess")
@Api(value="工序基本档 前端控制器")
public class BaseProcessController {
    @Autowired
    BaseProcessService baseProcessService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工序基本档列表")
    @UserOperationLog("工序基本档列表")
    public ResponseMessage<PageUtil<BaseProcess>> list(Query query){
        PageUtil<BaseProcess> page = baseProcessService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工序基本档详情")
    @UserOperationLog("工序基本档详情")
    public ResponseMessage<BaseProcess> info(@PathVariable("id") String id){
        BaseProcess baseProcess = baseProcessService.findById(id).orElse(null);
        return ResponseMessage.ok(baseProcess);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工序基本档")
    @UserOperationLog("保存工序基本档")
    public ResponseMessage<BaseProcess> save(@RequestBody BaseProcess baseProcess){
        ValidatorUtil.validateEntity(baseProcess, AddGroup.class);
        baseProcess.setProcessId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseProcessService.save(baseProcess));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工序基本档")
    @UserOperationLog("更新工序基本档")
    public ResponseMessage<BaseProcess> update(@RequestBody BaseProcess baseProcess){
        ValidatorUtil.validateEntity(baseProcess, UpdateGroup.class);
        BaseProcess baseProcessOld = baseProcessService.findById(baseProcess.getProcessId()).orElse(null);
        if(baseProcessOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseProcess,baseProcessOld);
        return ResponseMessage.ok(baseProcessService.save(baseProcessOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工序基本档")
    @UserOperationLog("删除工序基本档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseProcessService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}