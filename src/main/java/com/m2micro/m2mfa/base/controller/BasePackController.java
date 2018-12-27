package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BasePackQuery;
import com.m2micro.m2mfa.base.service.BasePackService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BasePack;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 包装 前端控制器
 *
 * @author wanglei
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/base/basePack")
@Api(value = "包装api")
@Authorize
public class BasePackController {
    @Autowired
    BasePackService basePackService;
    @Autowired
    BasePartsService basePartsService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取包装列表")
    @UserOperationLog("获取包装列表")
    public ResponseMessage<PageUtil<BasePack>> list(BasePackQuery query) {
        PageUtil<BasePack> page = basePackService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/info/{partId}", method = RequestMethod.GET)
    @ApiOperation(value = "传入料件编号获取包装详情")
    @UserOperationLog("包装详情")
    public ResponseMessage<List<BasePack>> info(@PathVariable("partId") String partId) {
        if(basePartsService.selectpartNo(partId)==null){
            throw new MMException("料件编号不存在！");
        }
        return ResponseMessage.ok(basePackService.findByPartId(partId));
    }

//    /**
//     * 添加
//     */
//    @RequestMapping(value = "/save",method = RequestMethod.POST)
//    @ApiOperation(value="添加包装")
//    @UserOperationLog("添加包装")
//    public ResponseMessage<BasePack> save(@RequestBody BasePack basePack){
//        ValidatorUtil.validateEntity(basePack, AddGroup.class);
//        basePack.setId(UUIDUtil.getUUID());
//        if(basePackService.countByPartIdAndcAndCategory(basePack.getPartId(),basePack.getCategory())>0)
//        {
//            throw new MMException("料号包装类型不唯一！");
//        }
//        return ResponseMessage.ok(basePackService.save(basePack));
//    }

    /**
     * 添加料件的包装，list(四个包装类型一起)
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "添加包装")
    @UserOperationLog("添加包装")
    public ResponseMessage<List<BasePack>> save(@RequestBody List<BasePack> basePackList) {
        if (basePackList != null && basePackList.size() != 4) {
            throw new MMException("料号包装个数不等于4！");
        }

        HashSet<Integer> categorys = new HashSet<>();
        HashSet<String> partids = new HashSet<>();
        for (BasePack one : basePackList) {
            if(basePartsService.countByPartNo(one.getPartId())==0){
                throw new MMException("料件编号不存在！");
            }

            partids.add(one.getPartId());
            categorys.add(one.getCategory());
            ValidatorUtil.validateEntity(one, AddGroup.class);
            one.setId(UUIDUtil.getUUID());
            if (basePackService.countByPartIdAndCategory(one.getPartId(), one.getCategory()) > 0) {
                throw new MMException("料号包装类型不唯一！");
            }
        }
        if (categorys.size() != 4) {
            throw new MMException("料号包装类型重复！");
        }
        if(partids.size()!=1){
            throw new MMException("不是同一个料件编号！");
        }

        List<BasePack> basePacks = basePackService.saveAll(basePackList);
        return ResponseMessage.ok(basePacks);
    }

//    /**
//     * 更新
//     */
//    @RequestMapping(value = "/update",method = RequestMethod.POST)
//    @ApiOperation(value="更新包装")
//    @UserOperationLog("更新包装")
//    public ResponseMessage<BasePack> update(@RequestBody BasePack basePack){
//        ValidatorUtil.validateEntity(basePack, UpdateGroup.class);
//        BasePack basePackOld = basePackService.findById(basePack.getId()).orElse(null);
//        if(basePackOld==null){
//            throw new MMException("数据库不存在该记录");
//        }
//        if(basePackService.countByIdNotAndpAndPartIdAndcAndCategory(basePack.getId(),basePack.getPartId(),basePack.getCategory())>0)
//        {
//            throw new MMException("料号包装类型不唯一！");
//        }
//        PropertyUtil.copy(basePack,basePackOld);
//        return ResponseMessage.ok(basePackService.save(basePackOld));
//    }


    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新包装,传入list一个料件四个包装类型")
    @UserOperationLog("更新包装")
    public ResponseMessage<List<BasePack>> update(@RequestBody List<BasePack> basePackList) {
        if (basePackList != null && basePackList.size() != 4) {
            throw new MMException("料号包装个数不等于4！");
        }
        List<BasePack> oldList = new ArrayList<>();
        HashSet<Integer> categorys = new HashSet<>();
        HashSet<String> partids = new HashSet<>();
        for (BasePack one : basePackList) {
            partids.add(one.getPartId());
            categorys.add(one.getCategory());
            if(basePartsService.countByPartNo(one.getPartId())==0){
                throw new MMException("料件编号不存在！");
            }
            ValidatorUtil.validateEntity(one, UpdateGroup.class);
            BasePack basePackOld = basePackService.findById(one.getId()).orElse(null);
            if (basePackOld == null) {
                throw new MMException("数据库不存在该记录");
            }
            if (basePackService.countByIdNotAndPartIdAndCategory(one.getId(), one.getPartId(), one.getCategory()) > 0) {
                throw new MMException("料号包装类型不唯一！");
            }
            PropertyUtil.copy(one, basePackOld);
            oldList.add(basePackOld);
        }
        if (categorys.size() != 4) {
            throw new MMException("料号包装类型重复！");
        }
        if(partids.size()!=1){
            throw new MMException("不是同一个料件编号！");
        }
        return ResponseMessage.ok(basePackService.saveAll(oldList));
    }

//    /**
//     * 删除
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation(value = "删除包装")
//    @UserOperationLog("删除包装")
//    public ResponseMessage delete(@RequestBody String[] ids) {
//        basePackService.deleteByIds(ids);
//        return ResponseMessage.ok();
//    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除包装,传入的是料件编号id 数组")
    @UserOperationLog("删除包装")
    public ResponseMessage delete(@RequestBody List<String> partIds) {
        List<String> ids= basePackService.findByPartIdIn(partIds);
       // String[] idss = new String[ids.size()];
        String[] idx = ids.toArray(new String[0]);
        //String[] idss = (String[])ids.toArray();
        //String[] idss = ids.stream().toArray(String[]::new);
        basePackService.deleteByIds(idx);
        return ResponseMessage.ok();
    }
}