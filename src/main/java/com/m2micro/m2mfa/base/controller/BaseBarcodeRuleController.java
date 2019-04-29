package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.m2mfa.base.query.BaseBarcodeRuleQuery;
import com.m2micro.m2mfa.base.service.BaseBarcodeRuleService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.BaseBarcodeRuleObj;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 条形码主表定义 前端控制器
 * @author wanglei
 * @since 2018-12-29
 */
@RestController
@RequestMapping("/base/baseBarcodeRule")
@Api(value="条形码规则增删改查")
@Authorize
public class BaseBarcodeRuleController {
    @Autowired
    BaseBarcodeRuleService baseBarcodeRuleService;

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    @ApiOperation(value="条形码主表定义列表")
    @UserOperationLog("条形码主表定义列表")
    public ResponseMessage<PageUtil<BaseBarcodeRule>> list(BaseBarcodeRuleQuery query){
        PageUtil<BaseBarcodeRule> page = baseBarcodeRuleService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    @ApiOperation(value="条形码规则详情")
    @UserOperationLog("条形码规则详情")
    public ResponseMessage<BaseBarcodeRuleObj> info(@PathVariable("id") String id){
        BaseBarcodeRuleObj baseBarcodeRuleObj = baseBarcodeRuleService.findByRuleId(id);
        return ResponseMessage.ok(baseBarcodeRuleObj);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation(value="批量删除条形码规则")
    @UserOperationLog("批量删除条形码主表")
    public ResponseMessage delete(@RequestBody List<String> ids){

        return  baseBarcodeRuleService.deleteByIdIn(ids);
    }

    /**
     * 删除变量
     */
    @RequestMapping(value = "/deleteVar",method = RequestMethod.POST)
    @ApiOperation(value="批量删除条形码规则变量")
    @UserOperationLog("批量删除条形码变量")
    public ResponseMessage deleteVar(@RequestBody List<String> ids){
        baseBarcodeRuleService.deleteVal(ids);
        return ResponseMessage.ok();
    }

    /**
     * 添加或修改条形码规则
     */
    @RequestMapping(value = "/addAndUpdate",method = RequestMethod.POST)
    @ApiOperation(value="添加或修改条形码规则")
    @UserOperationLog("添加或修改条形码规则")
    public ResponseMessage<BaseBarcodeRuleObj> addAndUpdate(@RequestBody BaseBarcodeRuleObj baseBarcodeRuleObj){
        BaseBarcodeRuleObj baseBarcodeRuleObj1 = baseBarcodeRuleService.AddOrUpdate(baseBarcodeRuleObj);
        return ResponseMessage.ok(baseBarcodeRuleObj1);
    }

}
