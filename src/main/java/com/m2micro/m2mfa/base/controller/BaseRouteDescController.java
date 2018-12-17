package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.service.BaseRouteDescService;
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
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产途程单头 前端控制器
 * @author chenshuhong
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/base/baseRouteDesc")
@Api(value="生产途程单头 前端控制器")
public class BaseRouteDescController {
    @Autowired
    BaseRouteDescService baseRouteDescService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产途程单头列表")
    @UserOperationLog("生产途程单头列表")
    public ResponseMessage<PageUtil<BaseRouteDesc>> list(Query query){
        PageUtil<BaseRouteDesc> page = baseRouteDescService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产途程单头详情")
    @UserOperationLog("生产途程单头详情")
    public ResponseMessage<BaseRouteDesc> info(@PathVariable("id") String id){
        BaseRouteDesc baseRouteDesc = baseRouteDescService.findById(id).orElse(null);
        return ResponseMessage.ok(baseRouteDesc);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产途程单头")
    @UserOperationLog("保存生产途程单头")
    public ResponseMessage<BaseRouteDesc> save(@RequestBody BaseRouteDesc baseRouteDesc){
        ValidatorUtil.validateEntity(baseRouteDesc, AddGroup.class);
        baseRouteDesc.setRouteId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseRouteDescService.save(baseRouteDesc));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产途程单头")
    @UserOperationLog("更新生产途程单头")
    public ResponseMessage<BaseRouteDesc> update(@RequestBody BaseRouteDesc baseRouteDesc){
        ValidatorUtil.validateEntity(baseRouteDesc, UpdateGroup.class);
        BaseRouteDesc baseRouteDescOld = baseRouteDescService.findById(baseRouteDesc.getRouteId()).orElse(null);
        if(baseRouteDescOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseRouteDesc,baseRouteDescOld);
        return ResponseMessage.ok(baseRouteDescService.save(baseRouteDescOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产途程单头")
    @UserOperationLog("删除生产途程单头")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseRouteDescService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}