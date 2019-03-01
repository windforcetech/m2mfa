package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseAqlDefService;
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
import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽样标准(aql)-明细 前端控制器
 * @author liaotao
 * @since 2019-01-29
 */
@RestController
@RequestMapping("/base/baseAqlDef")
@Api(value="抽样标准(aql)-明细 前端控制器")
@Authorize
public class BaseAqlDefController {
    @Autowired
    BaseAqlDefService baseAqlDefService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="抽样标准(aql)-明细列表")
    @UserOperationLog("抽样标准(aql)-明细列表")
    public ResponseMessage<PageUtil<BaseAqlDef>> list(Query query){
        PageUtil<BaseAqlDef> page = baseAqlDefService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="抽样标准(aql)-明细详情")
    @UserOperationLog("抽样标准(aql)-明细详情")
    public ResponseMessage<BaseAqlDef> info(@PathVariable("id") String id){
        BaseAqlDef baseAqlDef = baseAqlDefService.findById(id).orElse(null);
        return ResponseMessage.ok(baseAqlDef);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存抽样标准(aql)-明细")
    @UserOperationLog("保存抽样标准(aql)-明细")
    public ResponseMessage<BaseAqlDef> save(@RequestBody BaseAqlDef baseAqlDef){
        ValidatorUtil.validateEntity(baseAqlDef, AddGroup.class);
        baseAqlDef.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseAqlDefService.save(baseAqlDef));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新抽样标准(aql)-明细")
    @UserOperationLog("更新抽样标准(aql)-明细")
    public ResponseMessage<BaseAqlDef> update(@RequestBody BaseAqlDef baseAqlDef){
        ValidatorUtil.validateEntity(baseAqlDef, UpdateGroup.class);
        BaseAqlDef baseAqlDefOld = baseAqlDefService.findById(baseAqlDef.getId()).orElse(null);
        if(baseAqlDefOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseAqlDef,baseAqlDefOld);
        return ResponseMessage.ok(baseAqlDefService.save(baseAqlDefOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除抽样标准(aql)-明细")
    @UserOperationLog("删除抽样标准(aql)-明细")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseAqlDefService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}