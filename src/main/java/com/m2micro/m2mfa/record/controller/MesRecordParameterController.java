package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordParameterService;
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
import com.m2micro.m2mfa.record.entity.MesRecordParameter;
import org.springframework.web.bind.annotation.RestController;

/**
 * .参数记录表 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordParameter")
@Api(value=".参数记录表 前端控制器")
@Authorize
public class MesRecordParameterController {
    @Autowired
    MesRecordParameterService mesRecordParameterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value=".参数记录表列表")
    @UserOperationLog(".参数记录表列表")
    public ResponseMessage<PageUtil<MesRecordParameter>> list(Query query){
        PageUtil<MesRecordParameter> page = mesRecordParameterService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value=".参数记录表详情")
    @UserOperationLog(".参数记录表详情")
    public ResponseMessage<MesRecordParameter> info(@PathVariable("id") String id){
        MesRecordParameter mesRecordParameter = mesRecordParameterService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordParameter);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存.参数记录表")
    @UserOperationLog("保存.参数记录表")
    public ResponseMessage<MesRecordParameter> save(@RequestBody MesRecordParameter mesRecordParameter){
        ValidatorUtil.validateEntity(mesRecordParameter, AddGroup.class);
        mesRecordParameter.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordParameterService.save(mesRecordParameter));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新.参数记录表")
    @UserOperationLog("更新.参数记录表")
    public ResponseMessage<MesRecordParameter> update(@RequestBody MesRecordParameter mesRecordParameter){
        ValidatorUtil.validateEntity(mesRecordParameter, UpdateGroup.class);
        MesRecordParameter mesRecordParameterOld = mesRecordParameterService.findById(mesRecordParameter.getId()).orElse(null);
        if(mesRecordParameterOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordParameter,mesRecordParameterOld);
        return ResponseMessage.ok(mesRecordParameterService.save(mesRecordParameterOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除.参数记录表")
    @UserOperationLog("删除.参数记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordParameterService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}