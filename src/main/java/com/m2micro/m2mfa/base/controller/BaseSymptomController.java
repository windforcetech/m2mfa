package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseSymptomService;
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
import com.m2micro.m2mfa.base.entity.BaseSymptom;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不良原因代码 前端控制器
 * @author liaotao
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/base/baseSymptom")
@Api(value="不良原因代码 前端控制器")
@Authorize
public class BaseSymptomController {
    @Autowired
    BaseSymptomService baseSymptomService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="不良原因代码列表")
    @UserOperationLog("不良原因代码列表")
    public ResponseMessage<PageUtil<BaseSymptom>> list(Query query){
        PageUtil<BaseSymptom> page = baseSymptomService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="不良原因代码详情")
    @UserOperationLog("不良原因代码详情")
    public ResponseMessage<BaseSymptom> info(@PathVariable("id") String id){
        BaseSymptom baseSymptom = baseSymptomService.findById(id).orElse(null);
        return ResponseMessage.ok(baseSymptom);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存不良原因代码")
    @UserOperationLog("保存不良原因代码")
    public ResponseMessage<BaseSymptom> save(@RequestBody BaseSymptom baseSymptom){
        ValidatorUtil.validateEntity(baseSymptom, AddGroup.class);
        baseSymptom.setSymptomCode(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseSymptomService.save(baseSymptom));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新不良原因代码")
    @UserOperationLog("更新不良原因代码")
    public ResponseMessage<BaseSymptom> update(@RequestBody BaseSymptom baseSymptom){
        ValidatorUtil.validateEntity(baseSymptom, UpdateGroup.class);
        BaseSymptom baseSymptomOld = baseSymptomService.findById(baseSymptom.getSymptomCode()).orElse(null);
        if(baseSymptomOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseSymptom,baseSymptomOld);
        return ResponseMessage.ok(baseSymptomService.save(baseSymptomOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除不良原因代码")
    @UserOperationLog("删除不良原因代码")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseSymptomService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}