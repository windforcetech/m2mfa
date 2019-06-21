package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordMachineService;
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
import com.m2micro.m2mfa.record.entity.MesRecordMachine;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机台抄读历史记录 前端控制器
 * @author chenshuhong
 * @since 2019-06-19
 */
@RestController
@RequestMapping("/record/mesRecordMachine")
@Api(value="机台抄读历史记录 前端控制器")
@Authorize
public class MesRecordMachineController {
    @Autowired
    MesRecordMachineService mesRecordMachineService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="机台抄读历史记录列表")
    @UserOperationLog("机台抄读历史记录列表")
    public ResponseMessage<PageUtil<MesRecordMachine>> list(Query query){
        PageUtil<MesRecordMachine> page = mesRecordMachineService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="机台抄读历史记录详情")
    @UserOperationLog("机台抄读历史记录详情")
    public ResponseMessage<MesRecordMachine> info(@PathVariable("id") String id){
        MesRecordMachine mesRecordMachine = mesRecordMachineService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordMachine);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存机台抄读历史记录")
    @UserOperationLog("保存机台抄读历史记录")
    public ResponseMessage<MesRecordMachine> save(@RequestBody MesRecordMachine mesRecordMachine){
        ValidatorUtil.validateEntity(mesRecordMachine, AddGroup.class);
        mesRecordMachine.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordMachineService.save(mesRecordMachine));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新机台抄读历史记录")
    @UserOperationLog("更新机台抄读历史记录")
    public ResponseMessage<MesRecordMachine> update(@RequestBody MesRecordMachine mesRecordMachine){
        ValidatorUtil.validateEntity(mesRecordMachine, UpdateGroup.class);
        MesRecordMachine mesRecordMachineOld = mesRecordMachineService.findById(mesRecordMachine.getId()).orElse(null);
        if(mesRecordMachineOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordMachine,mesRecordMachineOld);
        return ResponseMessage.ok(mesRecordMachineService.save(mesRecordMachineOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除机台抄读历史记录")
    @UserOperationLog("删除机台抄读历史记录")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordMachineService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}