package com.m2micro.m2mfa.base.controller;

import com.m2micro.m2mfa.base.query.BaseStationQuery;
import com.m2micro.m2mfa.base.service.BaseStationService;
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
import com.m2micro.m2mfa.base.entity.BaseStation;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工位基本档 前端控制器
 * @author liaotao
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/base/baseStation")
@Api(value="工位基本档 前端控制器")
public class BaseStationController {
    @Autowired
    BaseStationService baseStationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工位基本档列表")
    @UserOperationLog("工位基本档列表")
    public ResponseMessage<PageUtil<BaseStation>> list(BaseStationQuery query){
        PageUtil<BaseStation> page = baseStationService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工位基本档详情")
    @UserOperationLog("工位基本档详情")
    public ResponseMessage<BaseStation> info(@PathVariable("id") String id){
        BaseStation baseStation = baseStationService.findById(id).orElse(null);
        return ResponseMessage.ok(baseStation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工位基本档")
    @UserOperationLog("保存工位基本档")
    public ResponseMessage<BaseStation> save(@RequestBody BaseStation baseStation){
        return ResponseMessage.ok(baseStationService.saveEntity(baseStation));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工位基本档")
    @UserOperationLog("更新工位基本档")
    public ResponseMessage<BaseStation> update(@RequestBody BaseStation baseStation){
        return ResponseMessage.ok(baseStationService.updateEntity(baseStation));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工位基本档")
    @UserOperationLog("删除工位基本档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseStationService.deleteAll(ids);
        return ResponseMessage.ok();
    }

}