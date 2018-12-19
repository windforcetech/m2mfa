package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseRouteQuery;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产途程单头 前端控制器
 * @author chenshuhong
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/base/baseRouteDesc")
@Api(value="生产途程单头 前端控制器")
@Authorize
public class BaseRouteDescController {
    @Autowired
    BaseRouteDescService baseRouteDescService;


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value=" 添加工艺")
    @UserOperationLog("添加工艺")
    public ResponseMessage save(@RequestBody BaseRoutevo routevo){
        return baseRouteDescService.save(routevo.getBaseRouteDesc(),routevo.getBaseRouteDef(),routevo.getBasePageElemen())==true ? ResponseMessage.ok(" 添加工艺成功。") : ResponseMessage.error(" 工艺已存在。");
    }



    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value="修改工艺")
    @UserOperationLog("修改工艺")
    public ResponseMessage update(@RequestBody BaseRoutevo routevo){
        return  baseRouteDescService.update(routevo.getBaseRouteDesc(),routevo.getBaseRouteDef(),routevo.getBasePageElemen()) == true ?ResponseMessage.ok("修改工艺成功。"):ResponseMessage.error("工艺编号不存在修改工艺失败。");
    }


    /**
     * 查询
     */
    @PostMapping("/list")
    @ApiOperation(value="查询工艺")
    @UserOperationLog("查询工艺")
    public ResponseMessage<PageUtil<BaseRouteDesc>> list(@RequestBody BaseRouteQuery query){
        return ResponseMessage.ok(baseRouteDescService.list(query));
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value=" 删除工艺")
    @UserOperationLog("删除工艺")
    public ResponseMessage delete(@ApiParam(required = true,value = "工艺Id")  @RequestParam(required = true) String routeId  ){
        return baseRouteDescService.delete(routeId);
    }



}