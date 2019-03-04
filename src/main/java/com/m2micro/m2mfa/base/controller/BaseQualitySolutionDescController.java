package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseQualitySolutionDescQuery;
import com.m2micro.m2mfa.base.service.BaseQualitySolutionDescService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.AqlDescSelect;
import com.m2micro.m2mfa.base.vo.BaseQualitySolutionDescModel;
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
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检验方案主档 前端控制器
 * @author liaotao
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/base/baseQualitySolutionDesc")
@Api(value="检验方案主档 前端控制器")
@Authorize
public class BaseQualitySolutionDescController {
    @Autowired
    BaseQualitySolutionDescService baseQualitySolutionDescService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="检验方案主档列表")
    @UserOperationLog("检验方案主档列表")
    public ResponseMessage<PageUtil<BaseQualitySolutionDesc>> list(BaseQualitySolutionDescQuery query){
        PageUtil<BaseQualitySolutionDesc> page = baseQualitySolutionDescService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="检验方案主档详情")
    @UserOperationLog("检验方案主档详情")
    public ResponseMessage<BaseQualitySolutionDesc> info(@PathVariable("id") String id){
        BaseQualitySolutionDesc baseQualitySolutionDesc = baseQualitySolutionDescService.findById(id).orElse(null);
        return ResponseMessage.ok(baseQualitySolutionDesc);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存检验方案主档")
    @UserOperationLog("保存检验方案主档")
    public ResponseMessage<BaseQualitySolutionDesc> save(@RequestBody BaseQualitySolutionDescModel baseQualitySolutionDescModel){
        baseQualitySolutionDescService.saveEntity(baseQualitySolutionDescModel);
        return ResponseMessage.ok();
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新检验方案主档")
    @UserOperationLog("更新检验方案主档")
    public ResponseMessage<BaseQualitySolutionDesc> update(@RequestBody BaseQualitySolutionDesc baseQualitySolutionDesc){
        ValidatorUtil.validateEntity(baseQualitySolutionDesc, UpdateGroup.class);
        BaseQualitySolutionDesc baseQualitySolutionDescOld = baseQualitySolutionDescService.findById(baseQualitySolutionDesc.getSolutionId()).orElse(null);
        if(baseQualitySolutionDescOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseQualitySolutionDesc,baseQualitySolutionDescOld);
        return ResponseMessage.ok(baseQualitySolutionDescService.save(baseQualitySolutionDescOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除检验方案主档")
    @UserOperationLog("删除检验方案主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseQualitySolutionDescService.deleteByIds(ids);
        return ResponseMessage.ok();
    }


    /**
     * 详情
     */
    @RequestMapping("/getAqlDesc")
    @ApiOperation(value="获取抽样方案")
    @UserOperationLog("获取抽样方案")
    public ResponseMessage<List<AqlDescSelect>> getAqlDesc(){
        return ResponseMessage.ok(baseQualitySolutionDescService.getAqlDesc());
    }
}