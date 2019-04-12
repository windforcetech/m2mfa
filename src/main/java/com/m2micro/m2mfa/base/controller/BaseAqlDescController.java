package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.entity.BaseAqlDef;
import com.m2micro.m2mfa.base.query.BaseAqlDescQuery;
import com.m2micro.m2mfa.base.service.BaseAqlDescService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.AqlDescvo;
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
import com.m2micro.m2mfa.base.entity.BaseAqlDesc;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽样标准(aql)-主档 前端控制器
 * @author liaotao
 * @since 2019-01-29
 */
@RestController
@RequestMapping("/base/baseAqlDesc")
@Api(value="抽样标准(aql)-主档 前端控制器")
@Authorize
public class BaseAqlDescController {
    @Autowired
    BaseAqlDescService baseAqlDescService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="抽样标准(aql)-主档列表")
    @UserOperationLog("抽样标准(aql)-主档列表")
    public ResponseMessage<PageUtil<BaseAqlDesc>> list(BaseAqlDescQuery query){
        PageUtil<BaseAqlDesc> page = baseAqlDescService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="抽样标准(aql)-主档详情")
    @UserOperationLog("抽样标准(aql)-主档详情")
    public ResponseMessage<AqlDescvo> info(@PathVariable("id") String id){
        AqlDescvo aqlDescvo = baseAqlDescService.selectAqlDes(id);
        return ResponseMessage.ok(aqlDescvo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存抽样标准(aql)-主档")
    @UserOperationLog("保存抽样标准(aql)-主档")
    public ResponseMessage<BaseAqlDesc> save(@RequestBody AqlDescvo aqlDescvo){

        baseAqlDescService.save(aqlDescvo);
        return ResponseMessage.ok();
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新抽样标准(aql)-主档")
    @UserOperationLog("更新抽样标准(aql)-主档")
    public ResponseMessage<BaseAqlDesc> update(@RequestBody AqlDescvo aqlDescvo){
        BaseAqlDesc baseAqlDesc = baseAqlDescService.findById(aqlDescvo.getBaseAqlDesc().getAqlId()).orElse(null);
        if(baseAqlDesc==null){
            throw new MMException("数据库不存在该记录");
        }
        baseAqlDescService.update(aqlDescvo);
        return ResponseMessage.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除抽样标准(aql)-主档")
    @UserOperationLog("删除抽样标准(aql)-主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        String msg =baseAqlDescService.deleteIds(ids);
        ResponseMessage rm = ResponseMessage.ok();
        if(msg!=null){
            rm.setMessage("抽样编码【"+msg.trim()+"】已产生业务,不允许删除！");
        }
        return  rm;
    }

}
