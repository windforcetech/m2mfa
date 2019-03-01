package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseUnit;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.base.service.BaseUnitService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private BaseUnitService baseUnitService;

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
    public ResponseMessage info(@PathVariable("id") String id){
        BaseMachine baseMachine = baseMachineService.findById(id).orElse(null);
        Map map = new HashMap();
        List<BaseUnit>  baseUnitList = baseUnitService.list();
        map.put("baseUnitList",baseUnitList);
        map.put("baseMachine",baseMachine);
        return ResponseMessage.ok(map);

    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存机台主档")
    @UserOperationLog("保存机台主档")
    public ResponseMessage<BaseMachine> save(@RequestBody BaseMachine baseMachine){
        return ResponseMessage.ok(baseMachineService.saveEntity(baseMachine));
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

        return  baseMachineService.delete(ids);
    }
    @RequestMapping("/addDetails")
    @ApiOperation(value="单位基本基本信息")
    @UserOperationLog("单位基本基本信息")
    public ResponseMessage addDetails(){
        Map map = new HashMap();
        List<BaseUnit>  baseUnitList = baseUnitService.list();
        map.put("baseUnitList",baseUnitList);
        return ResponseMessage.ok(map);
    }

    @RequestMapping("/getNames")
    @ApiOperation(value="获取机台名称下拉选项")
    @UserOperationLog("获取机台名称下拉选项")
    public ResponseMessage<List<SelectNode>> getNames(String machineId){
        return ResponseMessage.ok(baseMachineService.getNames(machineId));
    }

    @RequestMapping("/isMachineandDepartment")
    @ApiOperation(value="判断部门下面有关联机台")
    @UserOperationLog("判断部门下面有关联机台")
    public ResponseMessage<Boolean> isMachineandDepartment(String uuid){
        return ResponseMessage.ok(baseMachineService.isMachineandDepartment(uuid));
    }
}
