package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseStaffService;
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
import com.m2micro.m2mfa.base.entity.BaseStaff;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工（职员）表 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseStaff")
@Api(value="员工（职员）表 前端控制器")
@Authorize
public class BaseStaffController {
    @Autowired
    BaseStaffService baseStaffService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="员工（职员）表列表")
    @UserOperationLog("员工（职员）表列表")
    public ResponseMessage<PageUtil<BaseStaff>> list(Query query){
        PageUtil<BaseStaff> page = baseStaffService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="员工（职员）表详情")
    @UserOperationLog("员工（职员）表详情")
    public ResponseMessage<BaseStaff> info(@PathVariable("id") String id){
        BaseStaff baseStaff = baseStaffService.findById(id).orElse(null);
        return ResponseMessage.ok(baseStaff);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存员工（职员）表")
    @UserOperationLog("保存员工（职员）表")
    public ResponseMessage<BaseStaff> save(@RequestBody BaseStaff baseStaff){
        ValidatorUtil.validateEntity(baseStaff, AddGroup.class);
        baseStaff.setStaffId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseStaffService.save(baseStaff));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新员工（职员）表")
    @UserOperationLog("更新员工（职员）表")
    public ResponseMessage<BaseStaff> update(@RequestBody BaseStaff baseStaff){
        ValidatorUtil.validateEntity(baseStaff, UpdateGroup.class);
        BaseStaff baseStaffOld = baseStaffService.findById(baseStaff.getStaffId()).orElse(null);
        if(baseStaffOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseStaff,baseStaffOld);
        return ResponseMessage.ok(baseStaffService.save(baseStaffOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除员工（职员）表")
    @UserOperationLog("删除员工（职员）表")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseStaffService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}