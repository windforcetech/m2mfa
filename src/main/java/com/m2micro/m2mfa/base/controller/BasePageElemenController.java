package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.service.BasePageElemenService;
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
import com.m2micro.m2mfa.base.entity.BasePageElemen;
import org.springframework.web.bind.annotation.RestController;

/**
 * 页面元素 前端控制器
 * @author chenshuhong
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/base/basePageElemen")
@Api(value="页面元素 前端控制器")
public class BasePageElemenController {
    @Autowired
    BasePageElemenService basePageElemenService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="页面元素列表")
    @UserOperationLog("页面元素列表")
    public ResponseMessage<PageUtil<BasePageElemen>> list(Query query){
        PageUtil<BasePageElemen> page = basePageElemenService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="页面元素详情")
    @UserOperationLog("页面元素详情")
    public ResponseMessage<BasePageElemen> info(@PathVariable("id") String id){
        BasePageElemen basePageElemen = basePageElemenService.findById(id).orElse(null);
        return ResponseMessage.ok(basePageElemen);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存页面元素")
    @UserOperationLog("保存页面元素")
    public ResponseMessage<BasePageElemen> save(@RequestBody BasePageElemen basePageElemen){
        ValidatorUtil.validateEntity(basePageElemen, AddGroup.class);
        basePageElemen.setElemenId(UUIDUtil.getUUID());
        return ResponseMessage.ok(basePageElemenService.save(basePageElemen));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新页面元素")
    @UserOperationLog("更新页面元素")
    public ResponseMessage<BasePageElemen> update(@RequestBody BasePageElemen basePageElemen){
        ValidatorUtil.validateEntity(basePageElemen, UpdateGroup.class);
        BasePageElemen basePageElemenOld = basePageElemenService.findById(basePageElemen.getElemenId()).orElse(null);
        if(basePageElemenOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(basePageElemen,basePageElemenOld);
        return ResponseMessage.ok(basePageElemenService.save(basePageElemenOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除页面元素")
    @UserOperationLog("删除页面元素")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePageElemenService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}