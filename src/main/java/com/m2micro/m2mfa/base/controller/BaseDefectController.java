package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.query.BaseDefectQuery;
import com.m2micro.m2mfa.base.repository.BaseDefectRepository;
import com.m2micro.m2mfa.base.repository.BaseItemsTargetRepository;
import com.m2micro.m2mfa.base.service.BaseDefectService;
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
import com.m2micro.m2mfa.base.entity.BaseDefect;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不良現象代碼 前端控制器
 * @author liaotao
 * @since 2019-03-05
 */
@RestController
@RequestMapping("/base/baseDefect")
@Api(value="不良現象代碼 前端控制器")
@Authorize
public class BaseDefectController {
    @Autowired
    BaseDefectService baseDefectService;
    @Autowired
    BaseDefectRepository baseDefectRepository;
    @Autowired
    BaseItemsTargetRepository baseItemsTargetRepository;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="不良現象代碼列表")
    @UserOperationLog("不良現象代碼列表")
    public ResponseMessage<PageUtil<BaseDefect>> list(BaseDefectQuery query){
        PageUtil<BaseDefect> page = baseDefectService.listQuery(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="不良現象代碼详情")
    @UserOperationLog("不良現象代碼详情")
    public ResponseMessage<BaseDefect> info(@PathVariable("id") String id){
        BaseDefect baseDefect = baseDefectService.findById(id).orElse(null);
        baseDefect.setCategoryName(baseItemsTargetRepository.findById(baseDefect.getCategory()).orElse(null).getItemName());
        return ResponseMessage.ok(baseDefect);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存不良現象代碼")
    @UserOperationLog("保存不良現象代碼")
    public ResponseMessage<BaseDefect> save(@RequestBody BaseDefect baseDefect){
        if(!baseDefectRepository.findByEctCode(baseDefect.getEctCode()).isEmpty()){
            throw  new MMException("不良编号【"+baseDefect.getEctCode()+"】已被使用。");
        }
        ValidatorUtil.validateEntity(baseDefect, AddGroup.class);
        baseDefect.setEctId(UUIDUtil.getUUID());
        baseDefectService.save(baseDefect);
        return ResponseMessage.ok();
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新不良現象代碼")
    @UserOperationLog("更新不良現象代碼")
    public ResponseMessage<BaseDefect> update(@RequestBody BaseDefect baseDefect){
        ValidatorUtil.validateEntity(baseDefect, UpdateGroup.class);
        BaseDefect baseDefectOld = baseDefectService.findById(baseDefect.getEctId()).orElse(null);
        if(baseDefectOld==null){
            throw new MMException("数据库不存在该记录");
        }
        baseDefect.setEctCode(baseDefectOld.getEctCode());
        PropertyUtil.copy(baseDefect,baseDefectOld);
        return ResponseMessage.ok(baseDefectService.save(baseDefectOld));
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除不良現象代碼")
    @UserOperationLog("删除不良現象代碼")
    public ResponseMessage delete(@RequestBody String[] ids){
        String msg =  baseDefectService.deleteIds(ids);
        ResponseMessage rm = ResponseMessage.ok();
        if(msg.trim()!=""){
            rm.setMessage(msg.trim()+"已被引用不可删除。");
        }
        return  rm;
    }
}
