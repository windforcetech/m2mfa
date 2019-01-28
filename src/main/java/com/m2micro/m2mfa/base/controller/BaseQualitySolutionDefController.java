package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDefService;
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
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检验方案明细 前端控制器
 * @author liaotao
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/base/baseQualitySolutionDef")
@Api(value="检验方案明细 前端控制器")
@Authorize
public class BaseQualitySolutionDefController {
    @Autowired
    BaseQualitySolutionDefService baseQualitySolutionDefService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="检验方案明细列表")
    @UserOperationLog("检验方案明细列表")
    public ResponseMessage<PageUtil<BaseQualitySolutionDef>> list(Query query){
        PageUtil<BaseQualitySolutionDef> page = baseQualitySolutionDefService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="检验方案明细详情")
    @UserOperationLog("检验方案明细详情")
    public ResponseMessage<BaseQualitySolutionDef> info(@PathVariable("id") String id){
        BaseQualitySolutionDef baseQualitySolutionDef = baseQualitySolutionDefService.findById(id).orElse(null);
        return ResponseMessage.ok(baseQualitySolutionDef);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存检验方案明细")
    @UserOperationLog("保存检验方案明细")
    public ResponseMessage<BaseQualitySolutionDef> save(@RequestBody BaseQualitySolutionDef baseQualitySolutionDef){
        ValidatorUtil.validateEntity(baseQualitySolutionDef, AddGroup.class);
        baseQualitySolutionDef.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseQualitySolutionDefService.save(baseQualitySolutionDef));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新检验方案明细")
    @UserOperationLog("更新检验方案明细")
    public ResponseMessage<BaseQualitySolutionDef> update(@RequestBody BaseQualitySolutionDef baseQualitySolutionDef){
        ValidatorUtil.validateEntity(baseQualitySolutionDef, UpdateGroup.class);
        BaseQualitySolutionDef baseQualitySolutionDefOld = baseQualitySolutionDefService.findById(baseQualitySolutionDef.getId()).orElse(null);
        if(baseQualitySolutionDefOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseQualitySolutionDef,baseQualitySolutionDefOld);
        return ResponseMessage.ok(baseQualitySolutionDefService.save(baseQualitySolutionDefOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除检验方案明细")
    @UserOperationLog("删除检验方案明细")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseQualitySolutionDefService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}