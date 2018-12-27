package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.service.BaseItemsService;
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
import com.m2micro.m2mfa.base.entity.BaseItems;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参考资料维护主表 前端控制器
 * @author liaotao
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/base/baseItems")
@Api(value="参考资料维护主表 前端控制器")
public class BaseItemsController {
    @Autowired
    BaseItemsService baseItemsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="参考资料维护主表列表")
    @UserOperationLog("参考资料维护主表列表")
    public ResponseMessage<PageUtil<BaseItems>> list(Query query){
        PageUtil<BaseItems> page = baseItemsService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="参考资料维护主表详情")
    @UserOperationLog("参考资料维护主表详情")
    public ResponseMessage<BaseItems> info(@PathVariable("id") String id){
        BaseItems baseItems = baseItemsService.findById(id).orElse(null);
        return ResponseMessage.ok(baseItems);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存参考资料维护主表")
    @UserOperationLog("保存参考资料维护主表")
    public ResponseMessage<BaseItems> save(@RequestBody BaseItems baseItems){
        //ValidatorUtil.validateEntity(baseItems, AddGroup.class);
        baseItems.setItemId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseItemsService.save(baseItems));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新参考资料维护主表")
    @UserOperationLog("更新参考资料维护主表")
    public ResponseMessage<BaseItems> update(@RequestBody BaseItems baseItems){
        ValidatorUtil.validateEntity(baseItems, UpdateGroup.class);
        BaseItems baseItemsOld = baseItemsService.findById(baseItems.getItemId()).orElse(null);
        if(baseItemsOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseItems,baseItemsOld);
        return ResponseMessage.ok(baseItemsService.save(baseItemsOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除参考资料维护主表")
    @UserOperationLog("删除参考资料维护主表")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseItemsService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}