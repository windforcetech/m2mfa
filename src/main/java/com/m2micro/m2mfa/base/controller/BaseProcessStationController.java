package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseProcessStationService;
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
import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工序工位关系 前端控制器
 * @author chenshuhong
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/base/baseProcessStation")
@Api(value="工序工位关系 前端控制器")
@Authorize
public class BaseProcessStationController {
    @Autowired
    BaseProcessStationService baseProcessStationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工序工位关系列表")
    @UserOperationLog("工序工位关系列表")
    public ResponseMessage<PageUtil<BaseProcessStation>> list(Query query){
        PageUtil<BaseProcessStation> page = baseProcessStationService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工序工位关系详情")
    @UserOperationLog("工序工位关系详情")
    public ResponseMessage<BaseProcessStation> info(@PathVariable("id") String id){
        BaseProcessStation baseProcessStation = baseProcessStationService.findById(id).orElse(null);
        return ResponseMessage.ok(baseProcessStation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工序工位关系")
    @UserOperationLog("保存工序工位关系")
    public ResponseMessage<BaseProcessStation> save(@RequestBody BaseProcessStation baseProcessStation){
        ValidatorUtil.validateEntity(baseProcessStation, AddGroup.class);
        baseProcessStation.setPsId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseProcessStationService.save(baseProcessStation));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工序工位关系")
    @UserOperationLog("更新工序工位关系")
    public ResponseMessage<BaseProcessStation> update(@RequestBody BaseProcessStation baseProcessStation){
        ValidatorUtil.validateEntity(baseProcessStation, UpdateGroup.class);
        BaseProcessStation baseProcessStationOld = baseProcessStationService.findById(baseProcessStation.getPsId()).orElse(null);
        if(baseProcessStationOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseProcessStation,baseProcessStationOld);
        return ResponseMessage.ok(baseProcessStationService.save(baseProcessStationOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工序工位关系")
    @UserOperationLog("删除工序工位关系")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseProcessStationService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}