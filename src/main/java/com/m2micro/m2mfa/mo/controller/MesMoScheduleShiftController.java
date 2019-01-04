package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.mo.service.MesMoScheduleShiftService;
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
import com.m2micro.m2mfa.mo.entity.MesMoScheduleShift;
import org.springframework.web.bind.annotation.RestController;

/**
 *  前端控制器
 * @author liaotao
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/mo/mesMoScheduleShift")
@Api(value=" 前端控制器")
@Authorize
public class MesMoScheduleShiftController {
    @Autowired
    MesMoScheduleShiftService mesMoScheduleShiftService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="列表")
    @UserOperationLog("列表")
    public ResponseMessage<PageUtil<MesMoScheduleShift>> list(Query query){
        PageUtil<MesMoScheduleShift> page = mesMoScheduleShiftService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="详情")
    @UserOperationLog("详情")
    public ResponseMessage<MesMoScheduleShift> info(@PathVariable("id") String id){
        MesMoScheduleShift mesMoScheduleShift = mesMoScheduleShiftService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoScheduleShift);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存")
    @UserOperationLog("保存")
    public ResponseMessage<MesMoScheduleShift> save(@RequestBody MesMoScheduleShift mesMoScheduleShift){
        ValidatorUtil.validateEntity(mesMoScheduleShift, AddGroup.class);
        mesMoScheduleShift.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesMoScheduleShiftService.save(mesMoScheduleShift));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新")
    @UserOperationLog("更新")
    public ResponseMessage<MesMoScheduleShift> update(@RequestBody MesMoScheduleShift mesMoScheduleShift){
        ValidatorUtil.validateEntity(mesMoScheduleShift, UpdateGroup.class);
        MesMoScheduleShift mesMoScheduleShiftOld = mesMoScheduleShiftService.findById(mesMoScheduleShift.getId()).orElse(null);
        if(mesMoScheduleShiftOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoScheduleShift,mesMoScheduleShiftOld);
        return ResponseMessage.ok(mesMoScheduleShiftService.save(mesMoScheduleShiftOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除")
    @UserOperationLog("删除")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoScheduleShiftService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}