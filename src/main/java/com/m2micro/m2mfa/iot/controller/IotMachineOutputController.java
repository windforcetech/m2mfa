package com.m2micro.m2mfa.iot.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
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
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机台产出信息 前端控制器
 * @author liaotao
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/iot/iotMachineOutput")
@Api(value="机台产出信息 前端控制器")
@Authorize
public class IotMachineOutputController {
    @Autowired
    IotMachineOutputService iotMachineOutputService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="机台产出信息列表")
    @UserOperationLog("机台产出信息列表")
    public ResponseMessage<PageUtil<IotMachineOutput>> list(Query query){
        PageUtil<IotMachineOutput> page = iotMachineOutputService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="机台产出信息详情")
    @UserOperationLog("机台产出信息详情")
    public ResponseMessage<IotMachineOutput> info(@PathVariable("id") String id){
        IotMachineOutput iotMachineOutput = iotMachineOutputService.findById(id).orElse(null);
        return ResponseMessage.ok(iotMachineOutput);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存机台产出信息")
    @UserOperationLog("保存机台产出信息")
    public ResponseMessage<IotMachineOutput> save(@RequestBody IotMachineOutput iotMachineOutput){
        ValidatorUtil.validateEntity(iotMachineOutput, AddGroup.class);
        iotMachineOutput.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(iotMachineOutputService.save(iotMachineOutput));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新机台产出信息")
    @UserOperationLog("更新机台产出信息")
    public ResponseMessage<IotMachineOutput> update(@RequestBody IotMachineOutput iotMachineOutput){
        ValidatorUtil.validateEntity(iotMachineOutput, UpdateGroup.class);
        IotMachineOutput iotMachineOutputOld = iotMachineOutputService.findById(iotMachineOutput.getId()).orElse(null);
        if(iotMachineOutputOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(iotMachineOutput,iotMachineOutputOld);
        return ResponseMessage.ok(iotMachineOutputService.save(iotMachineOutputOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除机台产出信息")
    @UserOperationLog("删除机台产出信息")
    public ResponseMessage delete(@RequestBody String[] ids){
        iotMachineOutputService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}