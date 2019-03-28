package com.m2micro.m2mfa.barcode.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.barcode.service.BarcodePrintApplyService;
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
import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签打印表单 前端控制器
 * @author liaotao
 * @since 2019-03-27
 */
@RestController
@RequestMapping("/barcode/barcodePrintApply")
@Api(value="标签打印表单 前端控制器")
@Authorize
public class BarcodePrintApplyController {
    @Autowired
    BarcodePrintApplyService barcodePrintApplyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="标签打印表单列表")
    @UserOperationLog("标签打印表单列表")
    public ResponseMessage<PageUtil<BarcodePrintApply>> list(Query query){
        PageUtil<BarcodePrintApply> page = barcodePrintApplyService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="标签打印表单详情")
    @UserOperationLog("标签打印表单详情")
    public ResponseMessage<BarcodePrintApply> info(@PathVariable("id") String id){
        BarcodePrintApply barcodePrintApply = barcodePrintApplyService.findById(id).orElse(null);
        return ResponseMessage.ok(barcodePrintApply);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存标签打印表单")
    @UserOperationLog("保存标签打印表单")
    public ResponseMessage<BarcodePrintApply> save(@RequestBody BarcodePrintApply barcodePrintApply){
        ValidatorUtil.validateEntity(barcodePrintApply, AddGroup.class);
        barcodePrintApply.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(barcodePrintApplyService.save(barcodePrintApply));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新标签打印表单")
    @UserOperationLog("更新标签打印表单")
    public ResponseMessage<BarcodePrintApply> update(@RequestBody BarcodePrintApply barcodePrintApply){
        ValidatorUtil.validateEntity(barcodePrintApply, UpdateGroup.class);
        BarcodePrintApply barcodePrintApplyOld = barcodePrintApplyService.findById(barcodePrintApply.getId()).orElse(null);
        if(barcodePrintApplyOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(barcodePrintApply,barcodePrintApplyOld);
        return ResponseMessage.ok(barcodePrintApplyService.save(barcodePrintApplyOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除标签打印表单")
    @UserOperationLog("删除标签打印表单")
    public ResponseMessage delete(@RequestBody String[] ids){
        barcodePrintApplyService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}