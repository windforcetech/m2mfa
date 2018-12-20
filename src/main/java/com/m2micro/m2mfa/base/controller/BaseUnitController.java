package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseUnitService;
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
import com.m2micro.m2mfa.base.entity.BaseUnit;
import org.springframework.web.bind.annotation.RestController;

/**
 *  前端控制器
 * @author liaotao
 * @since 2018-12-20
 */
@RestController
@RequestMapping("/base/baseUnit")
@Api(value=" 前端控制器")
@Authorize
public class BaseUnitController {
    @Autowired
    BaseUnitService baseUnitService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="列表")
    @UserOperationLog("列表")
    public ResponseMessage<PageUtil<BaseUnit>> list(Query query){
        PageUtil<BaseUnit> page = baseUnitService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="详情")
    @UserOperationLog("详情")
    public ResponseMessage<BaseUnit> info(@PathVariable("id") String id){
        BaseUnit baseUnit = baseUnitService.findById(id).orElse(null);
        return ResponseMessage.ok(baseUnit);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存")
    @UserOperationLog("保存")
    public ResponseMessage<BaseUnit> save(@RequestBody BaseUnit baseUnit){
        ValidatorUtil.validateEntity(baseUnit, AddGroup.class);
        baseUnit.setUnitId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseUnitService.save(baseUnit));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新")
    @UserOperationLog("更新")
    public ResponseMessage<BaseUnit> update(@RequestBody BaseUnit baseUnit){
        ValidatorUtil.validateEntity(baseUnit, UpdateGroup.class);
        BaseUnit baseUnitOld = baseUnitService.findById(baseUnit.getUnitId()).orElse(null);
        if(baseUnitOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseUnit,baseUnitOld);
        return ResponseMessage.ok(baseUnitService.save(baseUnitOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除")
    @UserOperationLog("删除")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseUnitService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}