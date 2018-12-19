package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.service.BaseShiftService;
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
import com.m2micro.m2mfa.base.entity.BaseShift;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 班别基本资料 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseShift")
@Api(value="班别基本资料 前端控制器")
@Authorize
public class BaseShiftController {
    @Autowired
    BaseShiftService baseShiftService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="班别基本资料列表")
    @UserOperationLog("班别基本资料列表")
    public ResponseMessage<PageUtil<BaseShift>> list(Query query){
        PageUtil<BaseShift> page = baseShiftService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="班别基本资料详情")
    @UserOperationLog("班别基本资料详情")
    public ResponseMessage<BaseShift> info(@PathVariable("id") String id){
        BaseShift baseShift = baseShiftService.findById(id).orElse(null);
        return ResponseMessage.ok(baseShift);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存班别基本资料")
    @UserOperationLog("保存班别基本资料")
    public ResponseMessage<BaseShift> save(@RequestBody BaseShift baseShift){
        ValidatorUtil.validateEntity(baseShift, AddGroup.class);
        baseShift.setShiftId(UUIDUtil.getUUID());
        //校验编号唯一性
        List<BaseShift> list = baseShiftService.findByCodeAndShiftIdNot(baseShift.getCode(),"");
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return ResponseMessage.ok(baseShiftService.save(baseShift));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新班别基本资料")
    @UserOperationLog("更新班别基本资料")
    public ResponseMessage<BaseShift> update(@RequestBody BaseShift baseShift){
        ValidatorUtil.validateEntity(baseShift, UpdateGroup.class);
        BaseShift baseShiftOld = baseShiftService.findById(baseShift.getShiftId()).orElse(null);
        if(baseShiftOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //校验编号唯一性
        List<BaseShift> list = baseShiftService.findByCodeAndShiftIdNot(baseShift.getCode(),baseShift.getShiftId());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        PropertyUtil.copy(baseShift,baseShiftOld);
        return ResponseMessage.ok(baseShiftService.save(baseShiftOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除班别基本资料")
    @UserOperationLog("删除班别基本资料")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseShiftService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}