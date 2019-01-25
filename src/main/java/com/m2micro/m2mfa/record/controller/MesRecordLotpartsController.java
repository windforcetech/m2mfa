package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordLotpartsService;
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
import com.m2micro.m2mfa.record.entity.MesRecordLotparts;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加料记录表 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordLotparts")
@Api(value="加料记录表 前端控制器")
@Authorize
public class MesRecordLotpartsController {
    @Autowired
    MesRecordLotpartsService mesRecordLotpartsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="加料记录表列表")
    @UserOperationLog("加料记录表列表")
    public ResponseMessage<PageUtil<MesRecordLotparts>> list(Query query){
        PageUtil<MesRecordLotparts> page = mesRecordLotpartsService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="加料记录表详情")
    @UserOperationLog("加料记录表详情")
    public ResponseMessage<MesRecordLotparts> info(@PathVariable("id") String id){
        MesRecordLotparts mesRecordLotparts = mesRecordLotpartsService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordLotparts);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存加料记录表")
    @UserOperationLog("保存加料记录表")
    public ResponseMessage<MesRecordLotparts> save(@RequestBody MesRecordLotparts mesRecordLotparts){
        ValidatorUtil.validateEntity(mesRecordLotparts, AddGroup.class);
        mesRecordLotparts.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordLotpartsService.save(mesRecordLotparts));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新加料记录表")
    @UserOperationLog("更新加料记录表")
    public ResponseMessage<MesRecordLotparts> update(@RequestBody MesRecordLotparts mesRecordLotparts){
        ValidatorUtil.validateEntity(mesRecordLotparts, UpdateGroup.class);
        MesRecordLotparts mesRecordLotpartsOld = mesRecordLotpartsService.findById(mesRecordLotparts.getId()).orElse(null);
        if(mesRecordLotpartsOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordLotparts,mesRecordLotpartsOld);
        return ResponseMessage.ok(mesRecordLotpartsService.save(mesRecordLotpartsOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除加料记录表")
    @UserOperationLog("删除加料记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordLotpartsService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}