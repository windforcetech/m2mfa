package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BasePartQualitySolutionService;
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
import com.m2micro.m2mfa.base.entity.BasePartQualitySolution;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件品质方案关联 前端控制器
 * @author liaotao
 * @since 2019-03-05
 */
@RestController
@RequestMapping("/base/basePartQualitySolution")
@Api(value="料件品质方案关联 前端控制器")
@Authorize
public class BasePartQualitySolutionController {
    @Autowired
    BasePartQualitySolutionService basePartQualitySolutionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件品质方案关联列表")
    @UserOperationLog("料件品质方案关联列表")
    public ResponseMessage<PageUtil<BasePartQualitySolution>> list(Query query){
        PageUtil<BasePartQualitySolution> page = basePartQualitySolutionService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件品质方案关联详情")
    @UserOperationLog("料件品质方案关联详情")
    public ResponseMessage<BasePartQualitySolution> info(@PathVariable("id") String id){
        BasePartQualitySolution basePartQualitySolution = basePartQualitySolutionService.findById(id).orElse(null);
        return ResponseMessage.ok(basePartQualitySolution);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件品质方案关联")
    @UserOperationLog("保存料件品质方案关联")
    public ResponseMessage<BasePartQualitySolution> save(@RequestBody BasePartQualitySolution basePartQualitySolution){
        ValidatorUtil.validateEntity(basePartQualitySolution, AddGroup.class);
        basePartQualitySolution.setPartId(UUIDUtil.getUUID());
        return ResponseMessage.ok(basePartQualitySolutionService.save(basePartQualitySolution));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件品质方案关联")
    @UserOperationLog("更新料件品质方案关联")
    public ResponseMessage<BasePartQualitySolution> update(@RequestBody BasePartQualitySolution basePartQualitySolution){
        ValidatorUtil.validateEntity(basePartQualitySolution, UpdateGroup.class);
        BasePartQualitySolution basePartQualitySolutionOld = basePartQualitySolutionService.findById(basePartQualitySolution.getPartId()).orElse(null);
        if(basePartQualitySolutionOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(basePartQualitySolution,basePartQualitySolutionOld);
        return ResponseMessage.ok(basePartQualitySolutionService.save(basePartQualitySolutionOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件品质方案关联")
    @UserOperationLog("删除料件品质方案关联")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePartQualitySolutionService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}