package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.query.BasePartsQuery;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseParts;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 料件基本资料 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseParts")
@Api(value="料件基本资料 前端控制器")
@Authorize
public class BasePartsController {
    @Autowired
    BasePartsService basePartsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件基本资料列表")
    @UserOperationLog("料件基本资料列表")
    public ResponseMessage<PageUtil<BaseParts>> list(BasePartsQuery query){
        PageUtil<BaseParts> page = basePartsService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件基本资料详情")
    @UserOperationLog("料件基本资料详情")
    public ResponseMessage<BaseParts> info(@PathVariable("id") String id){
        BaseParts baseParts = basePartsService.findById(id).orElse(null);
        return ResponseMessage.ok(baseParts);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件基本资料")
    @UserOperationLog("保存料件基本资料")
    public ResponseMessage<BaseParts> save(@RequestBody BaseParts baseParts){
        ValidatorUtil.validateEntity(baseParts, AddGroup.class);
        baseParts.setPartId(UUIDUtil.getUUID());
        //校验编号唯一性
        List<BaseParts> list = basePartsService.findByPartNoAndPartIdNot(baseParts.getPartNo(), "");
        if(list!=null&&list.size()>0){
            throw new MMException("料件编号不唯一！");
        }
        return ResponseMessage.ok(basePartsService.save(baseParts));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件基本资料")
    @UserOperationLog("更新料件基本资料")
    public ResponseMessage<BaseParts> update(@RequestBody BaseParts baseParts){
        ValidatorUtil.validateEntity(baseParts, UpdateGroup.class);
        BaseParts basePartsOld = basePartsService.findById(baseParts.getPartId()).orElse(null);
        if(basePartsOld==null){
           throw new MMException("数据库不存在该记录");
        }
        //校验编号唯一性
        List<BaseParts> list = basePartsService.findByPartNoAndPartIdNot(baseParts.getPartNo(), baseParts.getPartId());
        if(list!=null&&list.size()>0){
            throw new MMException("料件编号【"+baseParts.getPartNo()+"】已存在");
        }
        PropertyUtil.copy(baseParts,basePartsOld);
        return ResponseMessage.ok(basePartsService.save(basePartsOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件基本资料")
    @UserOperationLog("删除料件基本资料")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePartsService.deleteAllByIds(ids);
        return ResponseMessage.ok();
    }

}