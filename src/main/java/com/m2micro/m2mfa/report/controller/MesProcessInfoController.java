package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.report.service.MesProcessInfoService;
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
import com.m2micro.m2mfa.report.entity.MesProcessInfo;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单工序信息 前端控制器
 * @author chenshuhong
 * @since 2019-06-12
 */
@RestController
@RequestMapping("/report/mesProcessInfo")
@Api(value="工单工序信息 前端控制器")
@Authorize
public class MesProcessInfoController {
    @Autowired
    MesProcessInfoService mesProcessInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工单工序信息列表")
    @UserOperationLog("工单工序信息列表")
    public ResponseMessage<PageUtil<MesProcessInfo>> list(Query query){
        PageUtil<MesProcessInfo> page = mesProcessInfoService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工单工序信息详情")
    @UserOperationLog("工单工序信息详情")
    public ResponseMessage<MesProcessInfo> info(@PathVariable("id") String id){
        MesProcessInfo mesProcessInfo = mesProcessInfoService.findById(id).orElse(null);
        return ResponseMessage.ok(mesProcessInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工单工序信息")
    @UserOperationLog("保存工单工序信息")
    public ResponseMessage<MesProcessInfo> save(@RequestBody MesProcessInfo mesProcessInfo){
        ValidatorUtil.validateEntity(mesProcessInfo, AddGroup.class);
        mesProcessInfo.setMesProcessInfoId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesProcessInfoService.save(mesProcessInfo));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工单工序信息")
    @UserOperationLog("更新工单工序信息")
    public ResponseMessage<MesProcessInfo> update(@RequestBody MesProcessInfo mesProcessInfo){
        ValidatorUtil.validateEntity(mesProcessInfo, UpdateGroup.class);
        MesProcessInfo mesProcessInfoOld = mesProcessInfoService.findById(mesProcessInfo.getMesProcessInfoId()).orElse(null);
        if(mesProcessInfoOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesProcessInfo,mesProcessInfoOld);
        return ResponseMessage.ok(mesProcessInfoService.save(mesProcessInfoOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工单工序信息")
    @UserOperationLog("删除工单工序信息")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesProcessInfoService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}