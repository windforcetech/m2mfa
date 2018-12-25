package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseProcessQuery;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseProcessService;
import com.m2micro.m2mfa.base.vo.Processvo;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工序基本档 前端控制器
 * @author chenshuhong
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/base/baseProcess")
@Api(value="工序基本档 前端控制器")
@Authorize
public class BaseProcessController {
    @Autowired
    BaseProcessService baseProcessService;

    /**
     * 添加工序
     */
    @PostMapping("/save")
    @ApiOperation(value="添加工序")
    @UserOperationLog("添加工序")
    public ResponseMessage save(@RequestBody Processvo processvo){
        return   baseProcessService.save(processvo.getBaseProcess(),processvo.getBaseProcessStations(),processvo.getBasePageElemen()) ==true ? ResponseMessage.ok("添加工序成功。"): ResponseMessage.error("工序已存在。");
    }


    /**
     * 修改工序
     */
    @PostMapping("/update")
    @ApiOperation(value="修改工序")
    @UserOperationLog("修改工序")
    public ResponseMessage update(@RequestBody Processvo processvo){
        return  baseProcessService.update(processvo.getBaseProcess(),processvo.getBaseProcessStations(),processvo.getBasePageElemen()) == true ?ResponseMessage.ok("修改工序成功。"):ResponseMessage.error("工序编号不存在修改工序失败。");
    }


    /**
     * 查询工序
     */
    @PostMapping("/list")
    @ApiOperation(value="查询工序")
    @UserOperationLog("查询工序")
    public ResponseMessage<PageUtil<BaseProcess>> list(@RequestBody  BaseProcessQuery query){
        return ResponseMessage.ok(baseProcessService.list(query));
    }



    /**
     * 删除工序
     */
    @PostMapping("/delete")
    @ApiOperation(value="删除工序")
    @UserOperationLog("删除工序")
    public ResponseMessage delete(@ApiParam(required = true,value = "工序Id")  @RequestParam(required = true) String processId ){
        return baseProcessService.delete(processId);
    }

}