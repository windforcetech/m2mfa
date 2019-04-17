package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordWipLogService;
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
import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在制记录表历史 前端控制器
 * @author liaotao
 * @since 2019-03-27
 */
@RestController
@RequestMapping("/record/mesRecordWipLog")
@Api(value="在制记录表历史 前端控制器")
@Authorize
public class MesRecordWipLogController {
    @Autowired
    MesRecordWipLogService mesRecordWipLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="在制记录表历史列表")
    @UserOperationLog("在制记录表历史列表")
    public ResponseMessage<PageUtil<MesRecordWipLog>> list(Query query){
        PageUtil<MesRecordWipLog> page = mesRecordWipLogService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="在制记录表历史详情")
    @UserOperationLog("在制记录表历史详情")
    public ResponseMessage<MesRecordWipLog> info(@PathVariable("id") String id){
        MesRecordWipLog mesRecordWipLog = mesRecordWipLogService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordWipLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存在制记录表历史")
    @UserOperationLog("保存在制记录表历史")
    public ResponseMessage<MesRecordWipLog> save(@RequestBody MesRecordWipLog mesRecordWipLog){
        ValidatorUtil.validateEntity(mesRecordWipLog, AddGroup.class);
        mesRecordWipLog.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordWipLogService.save(mesRecordWipLog));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新在制记录表历史")
    @UserOperationLog("更新在制记录表历史")
    public ResponseMessage<MesRecordWipLog> update(@RequestBody MesRecordWipLog mesRecordWipLog){
        ValidatorUtil.validateEntity(mesRecordWipLog, UpdateGroup.class);
        MesRecordWipLog mesRecordWipLogOld = mesRecordWipLogService.findById(mesRecordWipLog.getId()).orElse(null);
        if(mesRecordWipLogOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordWipLog,mesRecordWipLogOld);
        return ResponseMessage.ok(mesRecordWipLogService.save(mesRecordWipLogOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除在制记录表历史")
    @UserOperationLog("删除在制记录表历史")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordWipLogService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}