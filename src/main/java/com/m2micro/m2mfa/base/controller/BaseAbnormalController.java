package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseAbnormalService;
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
import com.m2micro.m2mfa.base.entity.BaseAbnormal;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常项目档 前端控制器
 * @author liaotao
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/base/baseAbnormal")
@Api(value="异常项目档 前端控制器")
@Authorize
public class BaseAbnormalController {
    @Autowired
    BaseAbnormalService baseAbnormalService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="异常项目档列表")
    @UserOperationLog("异常项目档列表")
    public ResponseMessage<PageUtil<BaseAbnormal>> list(Query query){
        PageUtil<BaseAbnormal> page = baseAbnormalService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="异常项目档详情")
    @UserOperationLog("异常项目档详情")
    public ResponseMessage<BaseAbnormal> info(@PathVariable("id") String id){
        BaseAbnormal baseAbnormal = baseAbnormalService.findById(id).orElse(null);
        return ResponseMessage.ok(baseAbnormal);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存异常项目档")
    @UserOperationLog("保存异常项目档")
    public ResponseMessage<BaseAbnormal> save(@RequestBody BaseAbnormal baseAbnormal){
        ValidatorUtil.validateEntity(baseAbnormal, AddGroup.class);
        baseAbnormal.setAbnormalId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseAbnormalService.save(baseAbnormal));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新异常项目档")
    @UserOperationLog("更新异常项目档")
    public ResponseMessage<BaseAbnormal> update(@RequestBody BaseAbnormal baseAbnormal){
        ValidatorUtil.validateEntity(baseAbnormal, UpdateGroup.class);
        BaseAbnormal baseAbnormalOld = baseAbnormalService.findById(baseAbnormal.getAbnormalId()).orElse(null);
        if(baseAbnormalOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseAbnormal,baseAbnormalOld);
        return ResponseMessage.ok(baseAbnormalService.save(baseAbnormalOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除异常项目档")
    @UserOperationLog("删除异常项目档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseAbnormalService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}