package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStationService;
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
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产排程工位 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/mo/mesMoScheduleStation")
@Api(value="生产排程工位 前端控制器")
@Authorize
public class MesMoScheduleStationController {
    @Autowired
    MesMoScheduleStationService mesMoScheduleStationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产排程工位列表")
    @UserOperationLog("生产排程工位列表")
    public ResponseMessage<PageUtil<MesMoScheduleStation>> list(Query query){
        PageUtil<MesMoScheduleStation> page = mesMoScheduleStationService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产排程工位详情")
    @UserOperationLog("生产排程工位详情")
    public ResponseMessage<MesMoScheduleStation> info(@PathVariable("id") String id){
        MesMoScheduleStation mesMoScheduleStation = mesMoScheduleStationService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoScheduleStation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产排程工位")
    @UserOperationLog("保存生产排程工位")
    public ResponseMessage<MesMoScheduleStation> save(@RequestBody MesMoScheduleStation mesMoScheduleStation){
        ValidatorUtil.validateEntity(mesMoScheduleStation, AddGroup.class);
        mesMoScheduleStation.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesMoScheduleStationService.save(mesMoScheduleStation));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产排程工位")
    @UserOperationLog("更新生产排程工位")
    public ResponseMessage<MesMoScheduleStation> update(@RequestBody MesMoScheduleStation mesMoScheduleStation){
        ValidatorUtil.validateEntity(mesMoScheduleStation, UpdateGroup.class);
        MesMoScheduleStation mesMoScheduleStationOld = mesMoScheduleStationService.findById(mesMoScheduleStation.getId()).orElse(null);
        if(mesMoScheduleStationOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoScheduleStation,mesMoScheduleStationOld);
        return ResponseMessage.ok(mesMoScheduleStationService.save(mesMoScheduleStationOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产排程工位")
    @UserOperationLog("删除生产排程工位")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoScheduleStationService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}