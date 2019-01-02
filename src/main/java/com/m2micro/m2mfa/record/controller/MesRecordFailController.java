package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordFailService;
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
import com.m2micro.m2mfa.record.entity.MesRecordFail;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不良输入记录 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordFail")
@Api(value="不良输入记录 前端控制器")
@Authorize
public class MesRecordFailController {
    @Autowired
    MesRecordFailService mesRecordFailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="不良输入记录列表")
    @UserOperationLog("不良输入记录列表")
    public ResponseMessage<PageUtil<MesRecordFail>> list(Query query){
        PageUtil<MesRecordFail> page = mesRecordFailService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="不良输入记录详情")
    @UserOperationLog("不良输入记录详情")
    public ResponseMessage<MesRecordFail> info(@PathVariable("id") String id){
        MesRecordFail mesRecordFail = mesRecordFailService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordFail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存不良输入记录")
    @UserOperationLog("保存不良输入记录")
    public ResponseMessage<MesRecordFail> save(@RequestBody MesRecordFail mesRecordFail){
        ValidatorUtil.validateEntity(mesRecordFail, AddGroup.class);
        mesRecordFail.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordFailService.save(mesRecordFail));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新不良输入记录")
    @UserOperationLog("更新不良输入记录")
    public ResponseMessage<MesRecordFail> update(@RequestBody MesRecordFail mesRecordFail){
        ValidatorUtil.validateEntity(mesRecordFail, UpdateGroup.class);
        MesRecordFail mesRecordFailOld = mesRecordFailService.findById(mesRecordFail.getId()).orElse(null);
        if(mesRecordFailOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordFail,mesRecordFailOld);
        return ResponseMessage.ok(mesRecordFailService.save(mesRecordFailOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除不良输入记录")
    @UserOperationLog("删除不良输入记录")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordFailService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}