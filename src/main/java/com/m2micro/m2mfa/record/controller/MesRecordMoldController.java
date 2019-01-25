package com.m2micro.m2mfa.record.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.record.service.MesRecordMoldService;
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
import com.m2micro.m2mfa.record.entity.MesRecordMold;
import org.springframework.web.bind.annotation.RestController;

/**
 * 上模记录表 前端控制器
 * @author liaotao
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/record/mesRecordMold")
@Api(value="上模记录表 前端控制器")
@Authorize
public class MesRecordMoldController {
    @Autowired
    MesRecordMoldService mesRecordMoldService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="上模记录表列表")
    @UserOperationLog("上模记录表列表")
    public ResponseMessage<PageUtil<MesRecordMold>> list(Query query){
        PageUtil<MesRecordMold> page = mesRecordMoldService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="上模记录表详情")
    @UserOperationLog("上模记录表详情")
    public ResponseMessage<MesRecordMold> info(@PathVariable("id") String id){
        MesRecordMold mesRecordMold = mesRecordMoldService.findById(id).orElse(null);
        return ResponseMessage.ok(mesRecordMold);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存上模记录表")
    @UserOperationLog("保存上模记录表")
    public ResponseMessage<MesRecordMold> save(@RequestBody MesRecordMold mesRecordMold){
        ValidatorUtil.validateEntity(mesRecordMold, AddGroup.class);
        mesRecordMold.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesRecordMoldService.save(mesRecordMold));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新上模记录表")
    @UserOperationLog("更新上模记录表")
    public ResponseMessage<MesRecordMold> update(@RequestBody MesRecordMold mesRecordMold){
        ValidatorUtil.validateEntity(mesRecordMold, UpdateGroup.class);
        MesRecordMold mesRecordMoldOld = mesRecordMoldService.findById(mesRecordMold.getId()).orElse(null);
        if(mesRecordMoldOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesRecordMold,mesRecordMoldOld);
        return ResponseMessage.ok(mesRecordMoldService.save(mesRecordMoldOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除上模记录表")
    @UserOperationLog("删除上模记录表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesRecordMoldService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}