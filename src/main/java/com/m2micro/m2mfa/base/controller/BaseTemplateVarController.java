package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseTemplateVarService;
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
import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模板变量 前端控制器
 * @author liaotao
 * @since 2019-01-22
 */
@RestController
@RequestMapping("/base/baseTemplateVar")
@Api(value="模板变量 前端控制器")
@Authorize
public class BaseTemplateVarController {
    @Autowired
    BaseTemplateVarService baseTemplateVarService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="模板变量列表")
    @UserOperationLog("模板变量列表")
    public ResponseMessage<PageUtil<BaseTemplateVar>> list(Query query){
        PageUtil<BaseTemplateVar> page = baseTemplateVarService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="模板变量详情")
    @UserOperationLog("模板变量详情")
    public ResponseMessage<BaseTemplateVar> info(@PathVariable("id") String id){
        BaseTemplateVar baseTemplateVar = baseTemplateVarService.findById(id).orElse(null);
        return ResponseMessage.ok(baseTemplateVar);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存模板变量")
    @UserOperationLog("保存模板变量")
    public ResponseMessage<BaseTemplateVar> save(@RequestBody BaseTemplateVar baseTemplateVar){
        ValidatorUtil.validateEntity(baseTemplateVar, AddGroup.class);
        baseTemplateVar.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseTemplateVarService.save(baseTemplateVar));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新模板变量")
    @UserOperationLog("更新模板变量")
    public ResponseMessage<BaseTemplateVar> update(@RequestBody BaseTemplateVar baseTemplateVar){
        ValidatorUtil.validateEntity(baseTemplateVar, UpdateGroup.class);
        BaseTemplateVar baseTemplateVarOld = baseTemplateVarService.findById(baseTemplateVar.getId()).orElse(null);
        if(baseTemplateVarOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseTemplateVar,baseTemplateVarOld);
        return ResponseMessage.ok(baseTemplateVarService.save(baseTemplateVarOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除模板变量")
    @UserOperationLog("删除模板变量")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseTemplateVarService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}