package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.service.PadScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * pad生产排程 前端控制器
 * @author liaotao
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/pad/padSchedule")
@Api(value="pad生产排程  前端控制器")
public class PadScheduleController {
    @Autowired
    PadScheduleService padScheduleService;

    /**
     * 获取当前员工下初始数据（排产单，工位）
     */
    @RequestMapping("/getInitData")
    @ApiOperation(value="获取当前员工下初始数据")
    @UserOperationLog("获取当前员工下初始数据")
    public ResponseMessage getInitData(String staffId){
        return ResponseMessage.ok(padScheduleService.getInitData(staffId));
    }

    /**
     * 获取当前员工下的排产单
     */
    @RequestMapping("/getMesMoScheduleByStaffId")
    @ApiOperation(value="获取当前员工下的排产单")
    @UserOperationLog("获取当前员工下的排产单")
    public ResponseMessage getMesMoScheduleByStaffId(String staffId){
        return ResponseMessage.ok(padScheduleService.getMesMoScheduleByStaffId(staffId));
    }

    /**
     * 获取待处理的工位
     */
    @RequestMapping("/getPendingStations")
    @ApiOperation(value="获取待处理的工位")
    @UserOperationLog("获取待处理的工位")
    public ResponseMessage<List<BaseStation>> getPendingStations(String staffId, String scheduleId){
        return ResponseMessage.ok(padScheduleService.getPendingStations(staffId, scheduleId));
    }


    /**
     * 获取操作栏相关信息
     */
    @RequestMapping("/getOperationInfo")
    @ApiOperation(value="获取操作栏相关信息")
    @UserOperationLog("获取操作栏相关信息")
    public ResponseMessage<OperationInfo> getOperationInfo(String staffId, String scheduleId, String stationId){
        return ResponseMessage.ok(padScheduleService.getOperationInfo(staffId, scheduleId,stationId));
    }

}