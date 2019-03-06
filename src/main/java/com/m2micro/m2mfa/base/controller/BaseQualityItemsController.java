package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseQualityItemsQuery;
import com.m2micro.m2mfa.base.service.BaseQualityItemsService;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsAddDetails;
import com.m2micro.m2mfa.base.vo.BaseQualityItemsModel;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检验项目 前端控制器
 * @author liaotao
 * @since 2019-01-28
 */
@RestController
@RequestMapping("/base/baseQualityItems")
@Api(value="检验项目 前端控制器")
@Authorize
public class BaseQualityItemsController {
    @Autowired
    BaseQualityItemsService baseQualityItemsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="检验项目列表")
    @UserOperationLog("检验项目列表")
    public ResponseMessage<PageUtil<BaseQualityItems>> list(BaseQualityItemsQuery query){
        return ResponseMessage.ok(baseQualityItemsService.list(query));
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="检验项目详情")
    @UserOperationLog("检验项目详情")
    public ResponseMessage<BaseQualityItemsModel> info(@PathVariable("id") String id){
        return ResponseMessage.ok(baseQualityItemsService.info(id));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存检验项目")
    @UserOperationLog("保存检验项目")
    public ResponseMessage<BaseQualityItems> save(@RequestBody BaseQualityItems baseQualityItems){
        return ResponseMessage.ok(baseQualityItemsService.saveEntity(baseQualityItems));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新检验项目")
    @UserOperationLog("更新检验项目")
    public ResponseMessage<BaseQualityItems> update(@RequestBody BaseQualityItems baseQualityItems){
        return ResponseMessage.ok(baseQualityItemsService.updateEntity(baseQualityItems));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除检验项目")
    @UserOperationLog("删除检验项目")
    public ResponseMessage delete(@RequestBody String[] ids){

        return baseQualityItemsService.deleteEntitys(ids);
    }

    /**
     * 新增是需要的数据
     */
    @RequestMapping("/addDetails")
    @ApiOperation(value="检验项目新增需要的数据")
    @UserOperationLog("检验项目新增需要的数据")
    public ResponseMessage<BaseQualityItemsAddDetails> addDetails(){
        return ResponseMessage.ok(baseQualityItemsService.addDetails());
    }

}