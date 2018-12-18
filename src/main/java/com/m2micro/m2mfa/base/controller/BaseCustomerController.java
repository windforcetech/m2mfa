package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseCustomerQuery;
import com.m2micro.m2mfa.base.service.BaseCustomerService;
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
import com.m2micro.m2mfa.base.entity.BaseCustomer;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户基本资料档 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseCustomer")
@Api(value="客户基本资料档 前端控制器")
public class BaseCustomerController {
    @Autowired
    BaseCustomerService baseCustomerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="客户基本资料档列表")
    @UserOperationLog("客户基本资料档列表")
    public ResponseMessage<PageUtil<BaseCustomer>> list(BaseCustomerQuery query){
        PageUtil<BaseCustomer> page = baseCustomerService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="客户基本资料档详情")
    @UserOperationLog("客户基本资料档详情")
    public ResponseMessage<BaseCustomer> info(@PathVariable("id") String id){
        BaseCustomer baseCustomer = baseCustomerService.findById(id).orElse(null);
        return ResponseMessage.ok(baseCustomer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存客户基本资料档")
    @UserOperationLog("保存客户基本资料档")
    public ResponseMessage<BaseCustomer> save(@RequestBody BaseCustomer baseCustomer){
        //ValidatorUtil.validateEntity(baseCustomer, AddGroup.class);
        baseCustomer.setCustomerId(UUIDUtil.getUUID());
        List<BaseCustomer> list = baseCustomerService.findByCodeAndCustomerIdNot(baseCustomer.getCode(),"");
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return ResponseMessage.ok(baseCustomerService.save(baseCustomer));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新客户基本资料档")
    @UserOperationLog("更新客户基本资料档")
    public ResponseMessage<BaseCustomer> update(@RequestBody BaseCustomer baseCustomer){
        ValidatorUtil.validateEntity(baseCustomer, UpdateGroup.class);
        BaseCustomer baseCustomerOld = baseCustomerService.findById(baseCustomer.getCustomerId()).orElse(null);
        if(baseCustomerOld==null){
            throw new MMException("数据库不存在该记录");
        }
        List<BaseCustomer> list = baseCustomerService.findByCodeAndCustomerIdNot(baseCustomer.getCode(),baseCustomer.getCustomerId());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        PropertyUtil.copy(baseCustomer,baseCustomerOld);
        return ResponseMessage.ok(baseCustomerService.save(baseCustomerOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除客户基本资料档")
    @UserOperationLog("删除客户基本资料档")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseCustomerService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}