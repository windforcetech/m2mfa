package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
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
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工排班表 前端控制器
 * @author liaotao
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/base/baseStaffshift")
@Api(value="员工排班表 前端控制器")
@Authorize
public class BaseStaffshiftController {
    @Autowired
    BaseStaffshiftService baseStaffshiftService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="员工排班表列表")
    @UserOperationLog("员工排班表列表")
    public ResponseMessage<PageUtil<BaseStaffshift>> list(Query query){
        PageUtil<BaseStaffshift> page = baseStaffshiftService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="员工排班表详情")
    @UserOperationLog("员工排班表详情")
    public ResponseMessage<BaseStaffshift> info(@PathVariable("id") String id){
        BaseStaffshift baseStaffshift = baseStaffshiftService.findById(id).orElse(null);
        return ResponseMessage.ok(baseStaffshift);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存员工排班表")
    @UserOperationLog("保存员工排班表")
    public ResponseMessage<BaseStaffshift> save(@RequestBody BaseStaffshift baseStaffshift){
        ValidatorUtil.validateEntity(baseStaffshift, AddGroup.class);
        baseStaffshift.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseStaffshiftService.save(baseStaffshift));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新员工排班表")
    @UserOperationLog("更新员工排班表")
    public ResponseMessage<BaseStaffshift> update(@RequestBody BaseStaffshift baseStaffshift){
        ValidatorUtil.validateEntity(baseStaffshift, UpdateGroup.class);
        BaseStaffshift baseStaffshiftOld = baseStaffshiftService.findById(baseStaffshift.getId()).orElse(null);
        if(baseStaffshiftOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseStaffshift,baseStaffshiftOld);
        return ResponseMessage.ok(baseStaffshiftService.save(baseStaffshiftOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除员工排班表")
    @UserOperationLog("删除员工排班表")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseStaffshiftService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}