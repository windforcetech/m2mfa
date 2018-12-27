package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordStaffService;
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
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员作业记录 前端控制器
 * @author wanglei
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/record/mesRecordStaff")
@Api(value="人员作业记录 前端控制器")
@Authorize
public class MesRecordStaffController {
    @Autowired
    MesRecordStaffService mesRecordStaffService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="人员作业记录列表")
    @UserOperationLog("人员作业记录列表")
    public ResponseMessage<PageUtil<MesRecordStaff>> list(Query query){
        PageUtil<MesRecordStaff> page = mesRecordStaffService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="人员作业记录详情")
    @UserOperationLog("人员作业记录详情")
    public ResponseMessage<MesRecordStaff> info(@PathVariable("id") String id){
        MesRecordStaff mesRecordStaff = mesRecordStaffService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordStaff);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存人员作业记录")
    @UserOperationLog("保存人员作业记录")
    public ResponseMessage<MesRecordStaff> save(@RequestBody MesRecordStaff mesRecordStaff){
        ValidatorUtil.validateEntity(mesRecordStaff, AddGroup.class);
        mesRecordStaff.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordStaffService.save(mesRecordStaff));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新人员作业记录")
    @UserOperationLog("更新人员作业记录")
    public ResponseMessage<MesRecordStaff> update(@RequestBody MesRecordStaff mesRecordStaff){
        ValidatorUtil.validateEntity(mesRecordStaff, UpdateGroup.class);
        MesRecordStaff mesRecordStaffOld = mesRecordStaffService.findById(mesRecordStaff.getId()).orElse(null);
        if(mesRecordStaffOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordStaff,mesRecordStaffOld);
        return ResponseMessage.ok(mesRecordStaffService.save(mesRecordStaffOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除人员作业记录")
    @UserOperationLog("删除人员作业记录")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordStaffService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}