package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
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
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产途程单身 前端控制器
 * @author chenshuhong
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/base/baseRouteDef")
@Api(value="生产途程单身 前端控制器")
@Authorize
public class BaseRouteDefController {
    @Autowired
    BaseRouteDefService baseRouteDefService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="生产途程单身列表")
    @UserOperationLog("生产途程单身列表")
    public ResponseMessage<PageUtil<BaseRouteDef>> list(Query query){
        PageUtil<BaseRouteDef> page = baseRouteDefService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="生产途程单身详情")
    @UserOperationLog("生产途程单身详情")
    public ResponseMessage<BaseRouteDef> info(@PathVariable("id") String id){
        BaseRouteDef baseRouteDef = baseRouteDefService.findById(id).orElse(null);
        return ResponseMessage.ok(baseRouteDef);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存生产途程单身")
    @UserOperationLog("保存生产途程单身")
    public ResponseMessage<BaseRouteDef> save(@RequestBody BaseRouteDef baseRouteDef){
        ValidatorUtil.validateEntity(baseRouteDef, AddGroup.class);
        baseRouteDef.setRouteDefId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseRouteDefService.save(baseRouteDef));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新生产途程单身")
    @UserOperationLog("更新生产途程单身")
    public ResponseMessage<BaseRouteDef> update(@RequestBody BaseRouteDef baseRouteDef){
        ValidatorUtil.validateEntity(baseRouteDef, UpdateGroup.class);
        BaseRouteDef baseRouteDefOld = baseRouteDefService.findById(baseRouteDef.getRouteDefId()).orElse(null);
        if(baseRouteDefOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseRouteDef,baseRouteDefOld);
        return ResponseMessage.ok(baseRouteDefService.save(baseRouteDefOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除生产途程单身")
    @UserOperationLog("删除生产途程单身")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseRouteDefService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}