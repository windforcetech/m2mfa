package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStaffService;
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
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产排程人员 前端控制器
 * @author liaotao
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/mo/mesMoScheduleStaff")
@Api(value="生产排程人员 前端控制器")
@Authorize
public class MesMoScheduleStaffController {
    @Autowired
    MesMoScheduleStaffService mesMoScheduleStaffService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产排程人员列表")
    @UserOperationLog("生产排程人员列表")
    public ResponseMessage<PageUtil<MesMoScheduleStaff>> list(Query query){
        PageUtil<MesMoScheduleStaff> page = mesMoScheduleStaffService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产排程人员详情")
    @UserOperationLog("生产排程人员详情")
    public ResponseMessage<MesMoScheduleStaff> info(@PathVariable("id") String id){
        MesMoScheduleStaff mesMoScheduleStaff = mesMoScheduleStaffService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoScheduleStaff);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产排程人员")
    @UserOperationLog("保存生产排程人员")
    public ResponseMessage<MesMoScheduleStaff> save(@RequestBody MesMoScheduleStaff mesMoScheduleStaff){
        ValidatorUtil.validateEntity(mesMoScheduleStaff, AddGroup.class);
        mesMoScheduleStaff.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesMoScheduleStaffService.save(mesMoScheduleStaff));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产排程人员")
    @UserOperationLog("更新生产排程人员")
    public ResponseMessage<MesMoScheduleStaff> update(@RequestBody MesMoScheduleStaff mesMoScheduleStaff){
        ValidatorUtil.validateEntity(mesMoScheduleStaff, UpdateGroup.class);
        MesMoScheduleStaff mesMoScheduleStaffOld = mesMoScheduleStaffService.findById(mesMoScheduleStaff.getId()).orElse(null);
        if(mesMoScheduleStaffOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoScheduleStaff,mesMoScheduleStaffOld);
        return ResponseMessage.ok(mesMoScheduleStaffService.save(mesMoScheduleStaffOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产排程人员")
    @UserOperationLog("删除生产排程人员")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoScheduleStaffService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}