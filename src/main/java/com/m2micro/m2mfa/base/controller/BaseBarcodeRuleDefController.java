package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleDefService;
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
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import org.springframework.web.bind.annotation.RestController;

/**
 * 条形码子序列字段定义 前端控制器
 * @author wanglei
 * @since 2018-12-29
 */
@RestController
@RequestMapping("/base/baseBarcodeRuleDef")
@Api(value="条形码子序列字段定义 前端控制器")
@Authorize
public class BaseBarcodeRuleDefController {
    @Autowired
    BaseBarcodeRuleDefService baseBarcodeRuleDefService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="条形码子序列字段定义列表")
    @UserOperationLog("条形码子序列字段定义列表")
    public ResponseMessage<PageUtil<BaseBarcodeRuleDef>> list(Query query){
        PageUtil<BaseBarcodeRuleDef> page = baseBarcodeRuleDefService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="条形码子序列字段定义详情")
    @UserOperationLog("条形码子序列字段定义详情")
    public ResponseMessage<BaseBarcodeRuleDef> info(@PathVariable("id") String id){
        BaseBarcodeRuleDef baseBarcodeRuleDef = baseBarcodeRuleDefService.findById(id).orElse(null);
        return ResponseMessage.ok(baseBarcodeRuleDef);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存条形码子序列字段定义")
    @UserOperationLog("保存条形码子序列字段定义")
    public ResponseMessage<BaseBarcodeRuleDef> save(@RequestBody BaseBarcodeRuleDef baseBarcodeRuleDef){
        ValidatorUtil.validateEntity(baseBarcodeRuleDef, AddGroup.class);
        baseBarcodeRuleDef.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseBarcodeRuleDefService.save(baseBarcodeRuleDef));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新条形码子序列字段定义")
    @UserOperationLog("更新条形码子序列字段定义")
    public ResponseMessage<BaseBarcodeRuleDef> update(@RequestBody BaseBarcodeRuleDef baseBarcodeRuleDef){
        ValidatorUtil.validateEntity(baseBarcodeRuleDef, UpdateGroup.class);
        BaseBarcodeRuleDef baseBarcodeRuleDefOld = baseBarcodeRuleDefService.findById(baseBarcodeRuleDef.getId()).orElse(null);
        if(baseBarcodeRuleDefOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseBarcodeRuleDef,baseBarcodeRuleDefOld);
        return ResponseMessage.ok(baseBarcodeRuleDefService.save(baseBarcodeRuleDefOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除条形码子序列字段定义")
    @UserOperationLog("删除条形码子序列字段定义")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseBarcodeRuleDefService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}