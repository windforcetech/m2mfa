package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordAbnormalService;
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
import com.m2micro.m2mfa.record.entity.MesRecordAbnormal;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常记录表 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordAbnormal")
@Api(value="异常记录表 前端控制器")
@Authorize
public class MesRecordAbnormalController {
    @Autowired
    MesRecordAbnormalService mesRecordAbnormalService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="异常记录表列表")
    @UserOperationLog("异常记录表列表")
    public ResponseMessage<PageUtil<MesRecordAbnormal>> list(Query query){
        PageUtil<MesRecordAbnormal> page = mesRecordAbnormalService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="异常记录表详情")
    @UserOperationLog("异常记录表详情")
    public ResponseMessage<MesRecordAbnormal> info(@PathVariable("id") String id){
        MesRecordAbnormal mesRecordAbnormal = mesRecordAbnormalService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordAbnormal);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存异常记录表")
    @UserOperationLog("保存异常记录表")
    public ResponseMessage<MesRecordAbnormal> save(@RequestBody MesRecordAbnormal mesRecordAbnormal){
        ValidatorUtil.validateEntity(mesRecordAbnormal, AddGroup.class);
        mesRecordAbnormal.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordAbnormalService.save(mesRecordAbnormal));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新异常记录表")
    @UserOperationLog("更新异常记录表")
    public ResponseMessage<MesRecordAbnormal> update(@RequestBody MesRecordAbnormal mesRecordAbnormal){
        ValidatorUtil.validateEntity(mesRecordAbnormal, UpdateGroup.class);
        MesRecordAbnormal mesRecordAbnormalOld = mesRecordAbnormalService.findById(mesRecordAbnormal.getId()).orElse(null);
        if(mesRecordAbnormalOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordAbnormal,mesRecordAbnormalOld);
        return ResponseMessage.ok(mesRecordAbnormalService.save(mesRecordAbnormalOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除异常记录表")
    @UserOperationLog("删除异常记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordAbnormalService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}