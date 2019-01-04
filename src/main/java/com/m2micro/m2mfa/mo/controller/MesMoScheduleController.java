package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.service.BaseMachineService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.MesMoScheduleInfoModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.query.ModescandpartsQuery;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.mo.vo.Productionorder;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 生产排程表表头 前端控制器
 * @author liaotao
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/mo/mesMoSchedule")
@Api(value="生产排程表表头 前端控制器")
@Authorize
public class MesMoScheduleController {
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    private MesMoDescService mesMoDescService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产排程表表头列表")
    @UserOperationLog("生产排程表表头列表")
    public ResponseMessage<PageUtil<MesMoScheduleModel>> list(MesMoScheduleQuery query){
        PageUtil<MesMoScheduleModel> page = mesMoScheduleService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产排程表表头详情")
    @UserOperationLog("生产排程表表头详情")
    public ResponseMessage<MesMoSchedule> info(@PathVariable("id") String id){
        MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoSchedule);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value="保存生产排程表表头")
    @UserOperationLog("保存生产排程表表头")
    public ResponseMessage save(@RequestBody Productionorder productionorder){
        mesMoScheduleService.save(productionorder.getMesMoSchedule(),productionorder.getMesMoScheduleStaffs(),productionorder.getMesMoScheduleProcesses(),productionorder.getMesMoScheduleStations());
        return ResponseMessage.ok();
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产排程表表头")
    @UserOperationLog("更新生产排程表表头")
    public ResponseMessage<MesMoSchedule> update(@RequestBody MesMoSchedule mesMoSchedule){
        ValidatorUtil.validateEntity(mesMoSchedule, UpdateGroup.class);
        MesMoSchedule mesMoScheduleOld = mesMoScheduleService.findById(mesMoSchedule.getScheduleId()).orElse(null);
        if(mesMoScheduleOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoSchedule,mesMoScheduleOld);
        return ResponseMessage.ok(mesMoScheduleService.save(mesMoScheduleOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产排程表表头")
    @UserOperationLog("删除生产排程表表头")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoScheduleService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 获取当前员工下的排产单
     */
    @RequestMapping("/getMesMoScheduleByStaffId")
    @ApiOperation(value="获取当前员工下的排产单")
    @UserOperationLog("获取当前员工下的排产单")
    public ResponseMessage getMesMoScheduleByStaffId(String staffId){
        return ResponseMessage.ok(mesMoScheduleService.getMesMoScheduleByStaffId(staffId));
    }

    /**
     * 获取待处理的工位
     */
    @RequestMapping("/getPendingStations")
    @ApiOperation(value="获取待处理的工位")
    @UserOperationLog("获取待处理的工位")
    public ResponseMessage<List<BaseStation>> getPendingStations(String staffId, String scheduleId){
        return ResponseMessage.ok(mesMoScheduleService.getPendingStations(staffId, scheduleId));
    }


    /**
     * 获取操作栏相关信息
     */
    @RequestMapping("/getOperationInfo")
    @ApiOperation(value="获取操作栏相关信息")
    @UserOperationLog("获取操作栏相关信息")
    public ResponseMessage<OperationInfo> getOperationInfo(String staffId, String scheduleId, String stationId){
        return ResponseMessage.ok(mesMoScheduleService.getOperationInfo(staffId, scheduleId,stationId));
    }

    @PostMapping("/schedulingDetails")
    @ApiOperation(value="工单信息")
    @UserOperationLog("工单信息")
    public ResponseMessage<PageUtil<MesMoDesc>> schedulingDetails(@RequestBody ModescandpartsQuery modescandpartsQuery){
        return ResponseMessage.ok(  mesMoDescService.schedulingDetails(modescandpartsQuery));
    }

    @PostMapping("/findbymoId")
    @ApiOperation(value="通过工单ID获取关联的图程数据")
    @UserOperationLog("通过工单ID获取关联的图程数据")
    public ResponseMessage<MesPartvo> findbymoId(@ApiParam(value = "moId",required=true) @RequestParam(required = true) String moId){

        return ResponseMessage.ok(mesMoScheduleService.findbyMoId(moId));
    }

    @PostMapping("/addDetails")
    @ApiOperation(value="排产单新增要返回的数据")
    @UserOperationLog("排产单新增要返回的数据")
    public ResponseMessage<MesMoScheduleInfoModel> addDetails(){
        return ResponseMessage.ok(mesMoScheduleService.addDetails());
    }


}