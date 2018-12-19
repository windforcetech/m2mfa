package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机台主档 前端控制器
 * @author liaotao
 * @since 2018-11-22
 */
@RestController
@RequestMapping("/base/baseMachine")
@Api(value="机台主档 前端控制器")
@Authorize
public class BaseMachineController {
    @Autowired
    BaseMachineService baseMachineService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="机台主档列表")
    @UserOperationLog("机台主档列表")
    public ResponseMessage<PageUtil<BaseMachine>> list(BaseMachineQuery query){
        PageUtil<BaseMachine> page = baseMachineService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="机台主档详情")
    @UserOperationLog("机台主档详情")
    public ResponseMessage<BaseMachine> info(@PathVariable("id") String id){
        BaseMachine baseMachine = baseMachineService.findById(id).orElse(null);
        return ResponseMessage.ok(baseMachine);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存机台主档")
    @UserOperationLog("保存机台主档")
    public ResponseMessage<BaseMachine> save(@RequestBody BaseMachine baseMachine){
        ValidatorUtil.validateEntity(baseMachine, AddGroup.class);
        baseMachine.setMachineId(UUIDUtil.getUUID());
        //校验code唯一性
        List<BaseMachine> list = baseMachineService.findAllByCode(baseMachine.getCode());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return ResponseMessage.ok(baseMachineService.save(baseMachine));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新机台主档")
    @UserOperationLog("更新机台主档")
    public ResponseMessage<BaseMachine> update(@RequestBody BaseMachine baseMachine){
        ValidatorUtil.validateEntity(baseMachine, UpdateGroup.class);
        BaseMachine baseMachineOld = baseMachineService.findById(baseMachine.getMachineId()).orElse(null);
        if(baseMachineOld==null){
            throw new MMException("数据库不存在该记录");
        }
        List<BaseMachine> list = baseMachineService.findByCodeAndMachineIdNot(baseMachine.getCode(),baseMachine.getMachineId());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        PropertyUtil.copy(baseMachine,baseMachineOld);
        return ResponseMessage.ok(baseMachineService.save(baseMachineOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除机台主档")
    @UserOperationLog("删除机台主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseMachineService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}