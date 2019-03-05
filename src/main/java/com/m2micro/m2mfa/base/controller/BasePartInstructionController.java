package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
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
import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作业指导书关联 前端控制器
 * @author chengshuhong
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/base/basePartInstruction")
@Api(value="作业指导书关联 前端控制器")
@Authorize
public class BasePartInstructionController {
    @Autowired
    BasePartInstructionService basePartInstructionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="作业指导书关联列表")
    @UserOperationLog("作业指导书关联列表")
    public ResponseMessage<PageUtil<BasePartInstruction>> list(Query query){
        PageUtil<BasePartInstruction> page = basePartInstructionService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="作业指导书关联详情")
    @UserOperationLog("作业指导书关联详情")
    public ResponseMessage<BasePartInstruction> info(@PathVariable("id") String id){
        BasePartInstruction basePartInstruction = basePartInstructionService.findById(id).orElse(null);
        return ResponseMessage.ok(basePartInstruction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存作业指导书关联")
    @UserOperationLog("保存作业指导书关联")
    public ResponseMessage<BasePartInstruction> save(@RequestBody BasePartInstruction basePartInstruction){
        ValidatorUtil.validateEntity(basePartInstruction, AddGroup.class);
        basePartInstruction.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(basePartInstructionService.save(basePartInstruction));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新作业指导书关联")
    @UserOperationLog("更新作业指导书关联")
    public ResponseMessage<BasePartInstruction> update(@RequestBody BasePartInstruction basePartInstruction){
        ValidatorUtil.validateEntity(basePartInstruction, UpdateGroup.class);
        BasePartInstruction basePartInstructionOld = basePartInstructionService.findById(basePartInstruction.getId()).orElse(null);
        if(basePartInstructionOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(basePartInstruction,basePartInstructionOld);
        return ResponseMessage.ok(basePartInstructionService.save(basePartInstructionOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除作业指导书关联")
    @UserOperationLog("删除作业指导书关联")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePartInstructionService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}