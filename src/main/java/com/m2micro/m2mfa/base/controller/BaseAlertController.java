package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseAlertService;
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
import com.m2micro.m2mfa.base.entity.BaseAlert;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预警方式设定 前端控制器
 * @author chenshuhong
 * @since 2019-04-02
 */
@RestController
@RequestMapping("/base/baseAlert")
@Api(value="预警方式设定 前端控制器")
@Authorize
public class BaseAlertController {
    @Autowired
    BaseAlertService baseAlertService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="预警方式设定列表")
    @UserOperationLog("预警方式设定列表")
    public ResponseMessage<PageUtil<BaseAlert>> list(Query query){
        PageUtil<BaseAlert> page = baseAlertService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="预警方式设定详情")
    @UserOperationLog("预警方式设定详情")
    public ResponseMessage<BaseAlert> info(@PathVariable("id") String id){
        BaseAlert baseAlert = baseAlertService.findById(id).orElse(null);
        return ResponseMessage.ok(baseAlert);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存预警方式设定")
    @UserOperationLog("保存预警方式设定")
    public ResponseMessage<BaseAlert> save(@RequestBody BaseAlert baseAlert){
        ValidatorUtil.validateEntity(baseAlert, AddGroup.class);
        baseAlert.setAlertId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseAlertService.save(baseAlert));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新预警方式设定")
    @UserOperationLog("更新预警方式设定")
    public ResponseMessage<BaseAlert> update(@RequestBody BaseAlert baseAlert){
        ValidatorUtil.validateEntity(baseAlert, UpdateGroup.class);
        BaseAlert baseAlertOld = baseAlertService.findById(baseAlert.getAlertId()).orElse(null);
        if(baseAlertOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseAlert,baseAlertOld);
        return ResponseMessage.ok(baseAlertService.save(baseAlertOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除预警方式设定")
    @UserOperationLog("删除预警方式设定")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseAlertService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}