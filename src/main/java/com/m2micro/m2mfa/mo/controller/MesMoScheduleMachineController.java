package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.mo.model.MesMoScheduleMachineModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.ScheduleMachineParaModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleMachineQuery;
import com.m2micro.m2mfa.mo.service.MesMoScheduleMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 11:29
 * @Description:
 */
@RestController
@RequestMapping("/mo/mesMoScheduleMachine")
@Api(value="排程机台调整 前端控制器")
@Authorize
public class MesMoScheduleMachineController {
    @Autowired
    MesMoScheduleMachineService mesMoScheduleMachineService;

    /**
     * 获取机台及部门
     */
    @RequestMapping("/getAllDepartAndMachine")
    @ApiOperation(value="获取所有已经绑定的机台及部门")
    @UserOperationLog("获取所有已经绑定的机台及部门")
    public ResponseMessage<TreeNode> getAllDepartAndMachine(){
        return ResponseMessage.ok(mesMoScheduleMachineService.getAllDepartAndMachine());
    }

    /**
     * 获取排程机台页面的排产单
     */
    @RequestMapping("/getScheduleForScheduleMachine")
    @ApiOperation(value="获取排程机台页面的排产单")
    @UserOperationLog("获取排程机台页面的排产单")
    public ResponseMessage<List<MesMoScheduleModel>> getScheduleForScheduleMachine(String machineId){
        return ResponseMessage.ok(mesMoScheduleMachineService.getScheduleForScheduleMachine(machineId));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="机台主档列表")
    @UserOperationLog("机台主档列表")
    public ResponseMessage<PageUtil<MesMoScheduleMachineModel>> list(MesMoScheduleMachineQuery query){
        PageUtil<MesMoScheduleMachineModel> page = mesMoScheduleMachineService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 排产单机台变更
     */
    @RequestMapping("/change")
    @ApiOperation(value="排产单机台变更")
    @UserOperationLog("排产单机台变更")
    public ResponseMessage change(@RequestBody ScheduleMachineParaModel scheduleMachineParaModel){
        return ResponseMessage.ok();
    }
}
