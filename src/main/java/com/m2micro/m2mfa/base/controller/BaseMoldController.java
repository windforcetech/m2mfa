package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.query.BaseMoldQuery;
import com.m2micro.m2mfa.base.service.BaseMoldService;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseMold;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模具主档 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseMold")
@Api(value="模具主档 前端控制器")
public class BaseMoldController {
    @Autowired
    BaseMoldService baseMoldService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="模具主档列表")
    @UserOperationLog("模具主档列表")
    public ResponseMessage<PageUtil<BaseMold>> list(BaseMoldQuery query){
        PageUtil<BaseMold> page = baseMoldService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="模具主档详情")
    @UserOperationLog("模具主档详情")
    public ResponseMessage<BaseMold> info(@PathVariable("id") String id){
        BaseMold baseMold = baseMoldService.findById(id).orElse(null);
        return ResponseMessage.ok(baseMold);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存模具主档")
    @UserOperationLog("保存模具主档")
    public ResponseMessage<BaseMold> save(@RequestBody BaseMold baseMold){
        ValidatorUtil.validateEntity(baseMold, AddGroup.class);
        baseMold.setMoldId(UUIDUtil.getUUID());
        //校验code唯一性
        List<BaseMold> list = baseMoldService.findAllByCode(baseMold.getCode());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return ResponseMessage.ok(baseMoldService.save(baseMold));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新模具主档")
    @UserOperationLog("更新模具主档")
    public ResponseMessage<BaseMold> update(@RequestBody BaseMold baseMold){
        ValidatorUtil.validateEntity(baseMold, UpdateGroup.class);

        BaseMold baseMoldOld = baseMoldService.findById(baseMold.getMoldId()).orElse(null);
        if(baseMoldOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //校验code唯一性
        List<BaseMold> list = baseMoldService.findByCodeAndMoldIdNot(baseMold.getCode(),baseMold.getMoldId());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        PropertyUtil.copy(baseMold,baseMoldOld);
        return ResponseMessage.ok(baseMoldService.save(baseMoldOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除模具主档")
    @UserOperationLog("删除模具主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        //根据ID删除模具，删除时查询【Mes_Record_Mold】表是否已产生业务，如果已有记录，提示用户已产生业务不允许删除。
        for (String id:ids){
            // 预留，是否产生业务
            if(false){
                throw new MMException("用户已产生业务不允许删除。");
            }
        }
        baseMoldService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}