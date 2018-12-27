package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BasePackService;
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
import com.m2micro.m2mfa.base.entity.BasePack;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包装 前端控制器
 * @author wanglei
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/base/basePack")
@Api(value="包装 前端控制器")
@Authorize
public class BasePackController {
    @Autowired
    BasePackService basePackService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="包装列表")
    @UserOperationLog("包装列表")
    public ResponseMessage<PageUtil<BasePack>> list(Query query){
        PageUtil<BasePack> page = basePackService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="包装详情")
    @UserOperationLog("包装详情")
    public ResponseMessage<BasePack> info(@PathVariable("id") String id){
        BasePack basePack = basePackService.findById(id).orElse(null);
        return ResponseMessage.ok(basePack);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存包装")
    @UserOperationLog("保存包装")
    public ResponseMessage<BasePack> save(@RequestBody BasePack basePack){
        ValidatorUtil.validateEntity(basePack, AddGroup.class);
        basePack.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(basePackService.save(basePack));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新包装")
    @UserOperationLog("更新包装")
    public ResponseMessage<BasePack> update(@RequestBody BasePack basePack){
        ValidatorUtil.validateEntity(basePack, UpdateGroup.class);
        BasePack basePackOld = basePackService.findById(basePack.getId()).orElse(null);
        if(basePackOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(basePack,basePackOld);
        return ResponseMessage.ok(basePackService.save(basePackOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除包装")
    @UserOperationLog("删除包装")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePackService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}