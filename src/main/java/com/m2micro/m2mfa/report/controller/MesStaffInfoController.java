package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.report.service.MesStaffInfoService;
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
import com.m2micro.m2mfa.report.entity.MesStaffInfo;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单职员信息 前端控制器
 * @author liaotao
 * @since 2019-06-13
 */
@RestController
@RequestMapping("/report/mesStaffInfo")
@Api(value="工单职员信息 前端控制器")
@Authorize
public class MesStaffInfoController {
    @Autowired
    MesStaffInfoService mesStaffInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工单职员信息列表")
    @UserOperationLog("工单职员信息列表")
    public ResponseMessage<PageUtil<MesStaffInfo>> list(Query query){
        PageUtil<MesStaffInfo> page = mesStaffInfoService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工单职员信息详情")
    @UserOperationLog("工单职员信息详情")
    public ResponseMessage<MesStaffInfo> info(@PathVariable("id") String id){
        MesStaffInfo mesStaffInfo = mesStaffInfoService.findById(id).orElse(null);
        return ResponseMessage.ok(mesStaffInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工单职员信息")
    @UserOperationLog("保存工单职员信息")
    public ResponseMessage<MesStaffInfo> save(@RequestBody MesStaffInfo mesStaffInfo){
        ValidatorUtil.validateEntity(mesStaffInfo, AddGroup.class);
        mesStaffInfo.setMesStaffInfoId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesStaffInfoService.save(mesStaffInfo));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工单职员信息")
    @UserOperationLog("更新工单职员信息")
    public ResponseMessage<MesStaffInfo> update(@RequestBody MesStaffInfo mesStaffInfo){
        ValidatorUtil.validateEntity(mesStaffInfo, UpdateGroup.class);
        MesStaffInfo mesStaffInfoOld = mesStaffInfoService.findById(mesStaffInfo.getMesStaffInfoId()).orElse(null);
        if(mesStaffInfoOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesStaffInfo,mesStaffInfoOld);
        return ResponseMessage.ok(mesStaffInfoService.save(mesStaffInfoOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工单职员信息")
    @UserOperationLog("删除工单职员信息")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesStaffInfoService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}