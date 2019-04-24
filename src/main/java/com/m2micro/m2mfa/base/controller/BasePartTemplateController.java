package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BasePartTemplateQuery;
import com.m2micro.m2mfa.base.service.BasePartTemplateService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.BasePartTemplateObj;
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
import com.m2micro.m2mfa.base.entity.BasePartTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件模板关联表 前端控制器
 *
 * @author liaotao
 * @since 2019-03-06
 */
@RestController
@RequestMapping("/base/basePartTemplate")
@Api(value = "料件模板关联表 前端控制器", description = "料件模板关联表 前端控制器")
@Authorize
public class BasePartTemplateController {
    @Autowired
    BasePartTemplateService basePartTemplateService;


    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除料件模板关联表")
    @UserOperationLog("删除料件模板关联表")
    public ResponseMessage delete(@RequestBody String[] ids) {
        basePartTemplateService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 添加
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加料件模板关联表")
    @UserOperationLog("添加料件模板关联表")
    public ResponseMessage<BasePartTemplate> add(@RequestBody BasePartTemplate basePartTemplate) {
        ValidatorUtil.validateEntity(basePartTemplate, AddGroup.class);
        basePartTemplate.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(basePartTemplateService.save(basePartTemplate));
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新料件模板关联表")
    @UserOperationLog("更新料件模板关联表")
    public ResponseMessage<BasePartTemplate> update(@RequestBody BasePartTemplate basePartTemplate) {
        ValidatorUtil.validateEntity(basePartTemplate, UpdateGroup.class);
        BasePartTemplate basePartTemplateOld = basePartTemplateService.findById(basePartTemplate.getId()).orElse(null);
        if (basePartTemplateOld == null) {
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(basePartTemplate, basePartTemplateOld);
        return ResponseMessage.ok(basePartTemplateService.save(basePartTemplateOld));
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "料件模板关联表列表")
    @UserOperationLog("料件模板关联表列表")
    public ResponseMessage<PageUtil<BasePartTemplateObj>> list(BasePartTemplateQuery query) {
        PageUtil<BasePartTemplateObj> page = basePartTemplateService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "料件模板关联表详情")
    @UserOperationLog("料件模板关联表详情")
    public ResponseMessage<BasePartTemplate> info(@PathVariable("id") String id) {
        BasePartTemplate basePartTemplate = basePartTemplateService.findById(id).orElse(null);
        return ResponseMessage.ok(basePartTemplate);
    }

}
