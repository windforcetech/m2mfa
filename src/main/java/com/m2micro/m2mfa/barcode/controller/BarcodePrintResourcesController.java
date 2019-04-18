package com.m2micro.m2mfa.barcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.barcode.service.BarcodePrintResourcesService;
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
import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签打印记录 前端控制器
 * @author liaotao
 * @since 2019-03-27
 */
@RestController
@RequestMapping("/barcode/barcodePrintResources")
@Api(value="标签打印记录 前端控制器")
@Authorize
public class BarcodePrintResourcesController {

    @Autowired
    BarcodePrintResourcesService barcodePrintResourcesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="标签打印记录列表")
    @UserOperationLog("标签打印记录列表")
    public ResponseMessage<PageUtil<BarcodePrintResources>> list(Query query){
        PageUtil<BarcodePrintResources> page = barcodePrintResourcesService.list(query);
        return ResponseMessage.ok(page);
    }


    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="标签打印记录详情")
    @UserOperationLog("标签打印记录详情")
    public ResponseMessage<BarcodePrintResources> info(@PathVariable("id") String id){
        BarcodePrintResources barcodePrintResources = barcodePrintResourcesService.findById(id).orElse(null);
        return ResponseMessage.ok(barcodePrintResources);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存标签打印记录")
    @UserOperationLog("保存标签打印记录")
    public ResponseMessage<BarcodePrintResources> save(@RequestBody BarcodePrintResources barcodePrintResources){
        ValidatorUtil.validateEntity(barcodePrintResources, AddGroup.class);
        barcodePrintResources.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(barcodePrintResourcesService.save(barcodePrintResources));
    }



    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新标签打印记录")
    @UserOperationLog("更新标签打印记录")
    public ResponseMessage<BarcodePrintResources> update(@RequestBody BarcodePrintResources barcodePrintResources){
        ValidatorUtil.validateEntity(barcodePrintResources, UpdateGroup.class);
        BarcodePrintResources barcodePrintResourcesOld = barcodePrintResourcesService.findById(barcodePrintResources.getId()).orElse(null);
        if(barcodePrintResourcesOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(barcodePrintResources,barcodePrintResourcesOld);
        return ResponseMessage.ok(barcodePrintResourcesService.save(barcodePrintResourcesOld));
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除标签打印记录")
    @UserOperationLog("删除标签打印记录")
    public ResponseMessage delete(@RequestBody String[] ids){
        barcodePrintResourcesService.deleteByIds(ids);
        return ResponseMessage.ok();
    }


}
