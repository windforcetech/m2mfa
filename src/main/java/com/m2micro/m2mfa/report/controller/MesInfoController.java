package com.m2micro.m2mfa.report.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.report.service.MesInfoService;
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
import com.m2micro.m2mfa.report.entity.MesInfo;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单信息 前端控制器
 * @author liaotao
 * @since 2019-06-12
 */
@RestController
@RequestMapping("/report/mesInfo")
@Api(value="工单信息 前端控制器")
@Authorize
public class MesInfoController {
    @Autowired
    MesInfoService mesInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工单信息列表")
    @UserOperationLog("工单信息列表")
    public ResponseMessage<PageUtil<MesInfo>> list(Query query){
        PageUtil<MesInfo> page = mesInfoService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工单信息详情")
    @UserOperationLog("工单信息详情")
    public ResponseMessage<MesInfo> info(@PathVariable("id") String id){
        MesInfo mesInfo = mesInfoService.findById(id).orElse(null);
        return ResponseMessage.ok(mesInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工单信息")
    @UserOperationLog("保存工单信息")
    public ResponseMessage<MesInfo> save(@RequestBody MesInfo mesInfo){
        ValidatorUtil.validateEntity(mesInfo, AddGroup.class);
        mesInfo.setMesInfoId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesInfoService.save(mesInfo));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工单信息")
    @UserOperationLog("更新工单信息")
    public ResponseMessage<MesInfo> update(@RequestBody MesInfo mesInfo){
        ValidatorUtil.validateEntity(mesInfo, UpdateGroup.class);
        MesInfo mesInfoOld = mesInfoService.findById(mesInfo.getMesInfoId()).orElse(null);
        if(mesInfoOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesInfo,mesInfoOld);
        return ResponseMessage.ok(mesInfoService.save(mesInfoOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工单信息")
    @UserOperationLog("删除工单信息")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesInfoService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}