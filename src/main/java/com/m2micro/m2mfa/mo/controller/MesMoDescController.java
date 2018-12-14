package com.m2micro.m2mfa.mo.controller;

import com.m2micro.m2mfa.mo.query.MesMoDescQuery;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
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
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工单主档 前端控制器
 * @author liaotao
 * @since 2018-12-10
 */
@RestController
@RequestMapping("/mo/mesMoDesc")
@Api(value="工单主档 前端控制器")
public class MesMoDescController {
    @Autowired
    MesMoDescService mesMoDescService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工单主档列表")
    @UserOperationLog("工单主档列表")
    public ResponseMessage<PageUtil<MesMoDesc>> list(MesMoDescQuery query){
        PageUtil<MesMoDesc> page = mesMoDescService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工单主档详情")
    @UserOperationLog("工单主档详情")
    public ResponseMessage<MesMoDesc> info(@PathVariable("id") String id){
        MesMoDesc mesMoDesc = mesMoDescService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoDesc);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工单主档")
    @UserOperationLog("保存工单主档")
    public ResponseMessage<MesMoDesc> save(@RequestBody MesMoDesc mesMoDesc){
        ValidatorUtil.validateEntity(mesMoDesc, AddGroup.class);
        mesMoDesc.setMoId(UUIDUtil.getUUID());
        List<MesMoDesc> list = mesMoDescService.findByMoNumberAndMoIdNot(mesMoDesc.getMoNumber(),"");
        if(list!=null&&list.size()>0){
            throw new MMException("工单号码不唯一！");
        }
        return ResponseMessage.ok(mesMoDescService.save(mesMoDesc));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工单主档")
    @UserOperationLog("更新工单主档")
    public ResponseMessage<MesMoDesc> update(@RequestBody MesMoDesc mesMoDesc){
        ValidatorUtil.validateEntity(mesMoDesc, UpdateGroup.class);
        MesMoDesc mesMoDescOld = mesMoDescService.findById(mesMoDesc.getMoId()).orElse(null);
        if(mesMoDescOld==null){
            throw new MMException("数据库不存在该记录");
        }
        List<MesMoDesc> list = mesMoDescService.findByMoNumberAndMoIdNot(mesMoDesc.getMoNumber(),mesMoDesc.getMoId());
        if(list!=null&&list.size()>0){
            throw new MMException("工单号码不唯一！");
        }
        PropertyUtil.copy(mesMoDesc,mesMoDescOld);
        return ResponseMessage.ok(mesMoDescService.save(mesMoDescOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工单主档")
    @UserOperationLog("删除工单主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoDescService.deleteAll(ids);
        return ResponseMessage.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/auditing/{id}")
    @ApiOperation(value="审核工单")
    @UserOperationLog("审核工单")
    public ResponseMessage<MesMoDesc> auditing(@PathVariable("id") String id){
        mesMoDescService.auditing(id);
        return ResponseMessage.ok();
    }


    /**
     * 取消审核
     */
    @RequestMapping("/cancel/{id}")
    @ApiOperation(value="取消审核工单")
    @UserOperationLog("取消审核工单")
    public ResponseMessage<MesMoDesc> cancel(@PathVariable("id") String id){
        mesMoDescService.cancel(id);
        return ResponseMessage.ok();
    }

    /**
     * 冻结
     */
    @RequestMapping("/frozen/{id}")
    @ApiOperation(value="冻结工单")
    @UserOperationLog("冻结工单")
    public ResponseMessage<MesMoDesc> frozen(@PathVariable("id") String id){
        mesMoDescService.frozen(id);
        return ResponseMessage.ok();
    }

    /**
     * 解冻
     */
    @RequestMapping("/unfreeze/{id}")
    @ApiOperation(value="解冻工单")
    @UserOperationLog("解冻工单")
    public ResponseMessage<MesMoDesc> unfreeze(@PathVariable("id") String id){
        mesMoDescService.unfreeze(id);
        return ResponseMessage.ok();
    }


    /**
     * 强制结案
     */
    @RequestMapping("/forceClose/{id}")
    @ApiOperation(value="工单强制结案")
    @UserOperationLog("工单强制结案")
    public ResponseMessage<MesMoDesc> forceClose(@PathVariable("id") String id){
        mesMoDescService.forceClose(id);
        return ResponseMessage.ok();
    }

}