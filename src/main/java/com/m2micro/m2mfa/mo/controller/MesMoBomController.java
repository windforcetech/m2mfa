package com.m2micro.m2mfa.mo.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.mo.service.MesMoBomService;
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
import com.m2micro.m2mfa.mo.entity.MesMoBom;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工单料表 前端控制器
 * @author liaotao
 * @since 2019-03-29
 */
@RestController
@RequestMapping("/mo/mesMoBom")
@Api(value="工单料表 前端控制器")
@Authorize
public class MesMoBomController {
    @Autowired
    MesMoBomService mesMoBomService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="工单料表列表")
    @UserOperationLog("工单料表列表")
    public ResponseMessage<PageUtil<MesMoBom>> list(Query query){
        PageUtil<MesMoBom> page = mesMoBomService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="工单料表详情")
    @UserOperationLog("工单料表详情")
    public ResponseMessage<MesMoBom> info(@PathVariable("id") String id){
        MesMoBom mesMoBom = mesMoBomService.findById(id).orElse(null);
        return ResponseMessage.ok(mesMoBom);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存工单料表")
    @UserOperationLog("保存工单料表")
    public ResponseMessage<MesMoBom> save(@RequestBody MesMoBom mesMoBom){
        ValidatorUtil.validateEntity(mesMoBom, AddGroup.class);
        mesMoBom.setMoId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesMoBomService.save(mesMoBom));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新工单料表")
    @UserOperationLog("更新工单料表")
    public ResponseMessage<MesMoBom> update(@RequestBody MesMoBom mesMoBom){
        ValidatorUtil.validateEntity(mesMoBom, UpdateGroup.class);
        MesMoBom mesMoBomOld = mesMoBomService.findById(mesMoBom.getMoId()).orElse(null);
        if(mesMoBomOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesMoBom,mesMoBomOld);
        return ResponseMessage.ok(mesMoBomService.save(mesMoBomOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除工单料表")
    @UserOperationLog("删除工单料表")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesMoBomService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}