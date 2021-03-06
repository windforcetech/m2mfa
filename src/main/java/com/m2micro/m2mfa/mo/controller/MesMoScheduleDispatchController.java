package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.ScheduleSequenceModel;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/4 15:47
 * @Description:
 */
@RestController
@RequestMapping("/mo/mesMoScheduleDispatch")
@Api(value="生产排程表表头 前端控制器")
@Authorize
public class MesMoScheduleDispatchController {
    @Autowired
    MesMoScheduleDispatchService mesMoScheduleDispatchService;

    /**
     * 获取机台及部门
     */
    @RequestMapping("/getAllDepartAndMachine")
    @ApiOperation(value="获取所有已经绑定的机台及部门")
    @UserOperationLog("获取所有已经绑定的机台及部门")
    public ResponseMessage<TreeNode> getAllDepartAndMachine(){
        return ResponseMessage.ok(mesMoScheduleDispatchService.getAllDepartAndMachine());
    }

    /**
     * 获取调度页面的排产单
     */
    @RequestMapping("/getScheduleDispatch")
    @ApiOperation(value="获取调度页面的排产单")
    @UserOperationLog("获取调度页面的排产单")
    public ResponseMessage<List<MesMoScheduleModel>> getScheduleDispatch(String machineId){
        return ResponseMessage.ok(mesMoScheduleDispatchService.getScheduleDispatch(machineId));
    }

    /**
     * 更新排产单顺序
     */
    @RequestMapping("/updateSequence")
    @ApiOperation(value="更新排产单顺序")
    @UserOperationLog("更新排产单顺序")
    public ResponseMessage updateSequence(@RequestBody ScheduleSequenceModel[] scheduleSequenceModels){
        mesMoScheduleDispatchService.updateSequence(scheduleSequenceModels);
        return ResponseMessage.ok();
    }
}
