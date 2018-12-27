package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
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
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import org.springframework.web.bind.annotation.RestController;

/**
 * 上工记录表 前端控制器
 * @author liaotao
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/record/mesRecordWork")
@Api(value="上工记录表 前端控制器")
@Authorize
public class MesRecordWorkController {
    @Autowired
    MesRecordWorkService mesRecordWorkService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="上工记录表列表")
    @UserOperationLog("上工记录表列表")
    public ResponseMessage<PageUtil<MesRecordWork>> list(Query query){
        PageUtil<MesRecordWork> page = mesRecordWorkService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="上工记录表详情")
    @UserOperationLog("上工记录表详情")
    public ResponseMessage<MesRecordWork> info(@PathVariable("id") String id){
        MesRecordWork mesRecordWork = mesRecordWorkService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordWork);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存上工记录表")
    @UserOperationLog("保存上工记录表")
    public ResponseMessage<MesRecordWork> save(@RequestBody MesRecordWork mesRecordWork){
        ValidatorUtil.validateEntity(mesRecordWork, AddGroup.class);
        mesRecordWork.setRwid(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordWorkService.save(mesRecordWork));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新上工记录表")
    @UserOperationLog("更新上工记录表")
    public ResponseMessage<MesRecordWork> update(@RequestBody MesRecordWork mesRecordWork){
        ValidatorUtil.validateEntity(mesRecordWork, UpdateGroup.class);
        MesRecordWork mesRecordWorkOld = mesRecordWorkService.findById(mesRecordWork.getRwid()).orElse(null);
        if(mesRecordWorkOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordWork,mesRecordWorkOld);
        return ResponseMessage.ok(mesRecordWorkService.save(mesRecordWorkOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除上工记录表")
    @UserOperationLog("删除上工记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordWorkService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}