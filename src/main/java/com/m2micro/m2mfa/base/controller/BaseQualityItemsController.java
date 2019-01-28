package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseQualityItemsService;
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
import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检验项目 前端控制器
 * @author liaotao
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/base/baseQualityItems")
@Api(value="检验项目 前端控制器")
@Authorize
public class BaseQualityItemsController {
    @Autowired
    BaseQualityItemsService baseQualityItemsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="检验项目列表")
    @UserOperationLog("检验项目列表")
    public ResponseMessage<PageUtil<BaseQualityItems>> list(Query query){
        PageUtil<BaseQualityItems> page = baseQualityItemsService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="检验项目详情")
    @UserOperationLog("检验项目详情")
    public ResponseMessage<BaseQualityItems> info(@PathVariable("id") String id){
        BaseQualityItems baseQualityItems = baseQualityItemsService.findById(id).orElse(null);
        return ResponseMessage.ok(baseQualityItems);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存检验项目")
    @UserOperationLog("保存检验项目")
    public ResponseMessage<BaseQualityItems> save(@RequestBody BaseQualityItems baseQualityItems){
        ValidatorUtil.validateEntity(baseQualityItems, AddGroup.class);
        baseQualityItems.setItemId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseQualityItemsService.save(baseQualityItems));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新检验项目")
    @UserOperationLog("更新检验项目")
    public ResponseMessage<BaseQualityItems> update(@RequestBody BaseQualityItems baseQualityItems){
        ValidatorUtil.validateEntity(baseQualityItems, UpdateGroup.class);
        BaseQualityItems baseQualityItemsOld = baseQualityItemsService.findById(baseQualityItems.getItemId()).orElse(null);
        if(baseQualityItemsOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseQualityItems,baseQualityItemsOld);
        return ResponseMessage.ok(baseQualityItemsService.save(baseQualityItemsOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除检验项目")
    @UserOperationLog("删除检验项目")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseQualityItemsService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}