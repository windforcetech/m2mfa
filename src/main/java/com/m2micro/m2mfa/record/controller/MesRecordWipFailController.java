package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordWipFailService;
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
import com.m2micro.m2mfa.record.entity.MesRecordWipFail;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在制不良记录表 前端控制器
 * @author chenshuhong
 * @since 2019-05-06
 */
@RestController
@RequestMapping("/record/mesRecordWipFail")
@Api(value="在制不良记录表 前端控制器")
@Authorize
public class MesRecordWipFailController {
    @Autowired
    MesRecordWipFailService mesRecordWipFailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="在制不良记录表列表")
    @UserOperationLog("在制不良记录表列表")
    public ResponseMessage<PageUtil<MesRecordWipFail>> list(Query query){
        PageUtil<MesRecordWipFail> page = mesRecordWipFailService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="在制不良记录表详情")
    @UserOperationLog("在制不良记录表详情")
    public ResponseMessage<MesRecordWipFail> info(@PathVariable("id") String id){
        MesRecordWipFail mesRecordWipFail = mesRecordWipFailService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordWipFail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存在制不良记录表")
    @UserOperationLog("保存在制不良记录表")
    public ResponseMessage<MesRecordWipFail> save(@RequestBody MesRecordWipFail mesRecordWipFail){
        ValidatorUtil.validateEntity(mesRecordWipFail, AddGroup.class);
        mesRecordWipFail.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordWipFailService.save(mesRecordWipFail));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新在制不良记录表")
    @UserOperationLog("更新在制不良记录表")
    public ResponseMessage<MesRecordWipFail> update(@RequestBody MesRecordWipFail mesRecordWipFail){
        ValidatorUtil.validateEntity(mesRecordWipFail, UpdateGroup.class);
        MesRecordWipFail mesRecordWipFailOld = mesRecordWipFailService.findById(mesRecordWipFail.getId()).orElse(null);
        if(mesRecordWipFailOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordWipFail,mesRecordWipFailOld);
        return ResponseMessage.ok(mesRecordWipFailService.save(mesRecordWipFailOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除在制不良记录表")
    @UserOperationLog("删除在制不良记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordWipFailService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}