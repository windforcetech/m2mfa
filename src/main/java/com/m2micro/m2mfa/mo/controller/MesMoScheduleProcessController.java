package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.mo.service.MesMoScheduleProcessService;
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
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产排程工序 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/mo/mesMoScheduleProcess")
@Api(value="生产排程工序 前端控制器")
@Authorize
public class MesMoScheduleProcessController {
    @Autowired
    MesMoScheduleProcessService mesMoScheduleProcessService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产排程工序列表")
    @UserOperationLog("生产排程工序列表")
    public ResponseMessage<PageUtil<MesMoScheduleProcess>> list(Query query){
        PageUtil<MesMoScheduleProcess> page = mesMoScheduleProcessService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产排程工序详情")
    @UserOperationLog("生产排程工序详情")
    public ResponseMessage<MesMoScheduleProcess> info(@PathVariable("id") String id){
        MesMoScheduleProcess mesMoScheduleProcess = mesMoScheduleProcessService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoScheduleProcess);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产排程工序")
    @UserOperationLog("保存生产排程工序")
    public ResponseMessage<MesMoScheduleProcess> save(@RequestBody MesMoScheduleProcess mesMoScheduleProcess){
        ValidatorUtil.validateEntity(mesMoScheduleProcess, AddGroup.class);
        mesMoScheduleProcess.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesMoScheduleProcessService.save(mesMoScheduleProcess));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产排程工序")
    @UserOperationLog("更新生产排程工序")
    public ResponseMessage<MesMoScheduleProcess> update(@RequestBody MesMoScheduleProcess mesMoScheduleProcess){
        ValidatorUtil.validateEntity(mesMoScheduleProcess, UpdateGroup.class);
        MesMoScheduleProcess mesMoScheduleProcessOld = mesMoScheduleProcessService.findById(mesMoScheduleProcess.getId()).orElse(null);
        if(mesMoScheduleProcessOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoScheduleProcess,mesMoScheduleProcessOld);
        return ResponseMessage.ok(mesMoScheduleProcessService.save(mesMoScheduleProcessOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产排程工序")
    @UserOperationLog("删除生产排程工序")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoScheduleProcessService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}