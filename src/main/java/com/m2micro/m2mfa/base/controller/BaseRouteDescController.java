package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseRouteQuery;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.base.vo.BaseRoutevo;;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
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
       return baseRouteDescService.save(routevo.getBaseRouteDesc(),routevo.getBaseRouteDefs(),routevo.getBasePageElemen())==true ? ResponseMessage.ok(" 添加工艺成功。") : ResponseMessage.error(" 工艺代码【"+routevo.getBaseRouteDesc().getRouteNo()+"】已存在。");
    }



    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value="修改工艺")
    @UserOperationLog("修改工艺")
    public ResponseMessage update(@RequestBody BaseRoutevo routevo){
        return  baseRouteDescService.update(routevo.getBaseRouteDesc(),routevo.getBaseRouteDefs(),routevo.getBasePageElemen()) == true ?ResponseMessage.ok("修改工艺成功。"):ResponseMessage.error(" 工艺代码【"+routevo.getBaseRouteDesc().getRouteNo()+"】不存在。");
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
     * 详情
     */
    @PostMapping("/info")
    @ApiOperation(value="工艺详情")
    @UserOperationLog("工艺详情")
    public ResponseMessage<BaseRoutevo> info(@ApiParam(value = "routeId",required=true) @RequestParam(required = true)   String routeId){
        return ResponseMessage.ok(baseRouteDescService.info(routeId));
    }




    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value=" 删除工艺")
    @UserOperationLog("删除工艺")
    public ResponseMessage delete(@RequestBody String[] ids ){
        String msgs="";
        for(int i =0;i<ids.length;i++){
         String msg=   baseRouteDescService.delete(ids[i]);
            if(!msg.trim().equals("")){
                msgs+=msg;
            }
        }
       ResponseMessage rm = ResponseMessage.ok();
        if(msgs.trim()!=""){
            rm.setResult(msgs.trim()+"已产生途程业务。");
        }

        return  rm;
    }




}
