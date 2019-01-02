package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordWipRecService;
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
import com.m2micro.m2mfa.record.entity.MesRecordWipRec;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在制记录表 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordWipRec")
@Api(value="在制记录表 前端控制器")
@Authorize
public class MesRecordWipRecController {
    @Autowired
    MesRecordWipRecService mesRecordWipRecService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="在制记录表列表")
    @UserOperationLog("在制记录表列表")
    public ResponseMessage<PageUtil<MesRecordWipRec>> list(Query query){
        PageUtil<MesRecordWipRec> page = mesRecordWipRecService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="在制记录表详情")
    @UserOperationLog("在制记录表详情")
    public ResponseMessage<MesRecordWipRec> info(@PathVariable("id") String id){
        MesRecordWipRec mesRecordWipRec = mesRecordWipRecService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordWipRec);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存在制记录表")
    @UserOperationLog("保存在制记录表")
    public ResponseMessage<MesRecordWipRec> save(@RequestBody MesRecordWipRec mesRecordWipRec){
        ValidatorUtil.validateEntity(mesRecordWipRec, AddGroup.class);
        mesRecordWipRec.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordWipRecService.save(mesRecordWipRec));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新在制记录表")
    @UserOperationLog("更新在制记录表")
    public ResponseMessage<MesRecordWipRec> update(@RequestBody MesRecordWipRec mesRecordWipRec){
        ValidatorUtil.validateEntity(mesRecordWipRec, UpdateGroup.class);
        MesRecordWipRec mesRecordWipRecOld = mesRecordWipRecService.findById(mesRecordWipRec.getId()).orElse(null);
        if(mesRecordWipRecOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordWipRec,mesRecordWipRecOld);
        return ResponseMessage.ok(mesRecordWipRecService.save(mesRecordWipRecOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除在制记录表")
    @UserOperationLog("删除在制记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordWipRecService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}