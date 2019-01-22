package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseTemplateService;
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
import com.m2micro.m2mfa.base.entity.BaseTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签模板 前端控制器
 * @author liaotao
 * @since 2019-01-22
 */
@RestController
@RequestMapping("/base/baseTemplate")
@Api(value="标签模板 前端控制器")
@Authorize
public class BaseTemplateController {
    @Autowired
    BaseTemplateService baseTemplateService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="标签模板列表")
    @UserOperationLog("标签模板列表")
    public ResponseMessage<PageUtil<BaseTemplate>> list(Query query){
        PageUtil<BaseTemplate> page = baseTemplateService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="标签模板详情")
    @UserOperationLog("标签模板详情")
    public ResponseMessage<BaseTemplate> info(@PathVariable("id") String id){
        BaseTemplate baseTemplate = baseTemplateService.findById(id).orElse(null);
        return ResponseMessage.ok(baseTemplate);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存标签模板")
    @UserOperationLog("保存标签模板")
    public ResponseMessage<BaseTemplate> save(@RequestBody BaseTemplate baseTemplate){
        ValidatorUtil.validateEntity(baseTemplate, AddGroup.class);
        baseTemplate.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseTemplateService.save(baseTemplate));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新标签模板")
    @UserOperationLog("更新标签模板")
    public ResponseMessage<BaseTemplate> update(@RequestBody BaseTemplate baseTemplate){
        ValidatorUtil.validateEntity(baseTemplate, UpdateGroup.class);
        BaseTemplate baseTemplateOld = baseTemplateService.findById(baseTemplate.getId()).orElse(null);
        if(baseTemplateOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseTemplate,baseTemplateOld);
        return ResponseMessage.ok(baseTemplateService.save(baseTemplateOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除标签模板")
    @UserOperationLog("删除标签模板")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseTemplateService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}