package com.m2micro.m2mfa.pr.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.pr.service.MesPartRouteProcessService;
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
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件途程设定工序 前端控制器
 * @author liaotao
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/pr/mesPartRouteProcess")
@Api(value="料件途程设定工序 前端控制器")
@Authorize
public class MesPartRouteProcessController {
    @Autowired
    MesPartRouteProcessService mesPartRouteProcessService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件途程设定工序列表")
    @UserOperationLog("料件途程设定工序列表")
    public ResponseMessage<PageUtil<MesPartRouteProcess>> list(Query query){
        PageUtil<MesPartRouteProcess> page = mesPartRouteProcessService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件途程设定工序详情")
    @UserOperationLog("料件途程设定工序详情")
    public ResponseMessage<MesPartRouteProcess> info(@PathVariable("id") String id){
        MesPartRouteProcess mesPartRouteProcess = mesPartRouteProcessService.findById(id).orElse(null);
        return ResponseMessage.ok(mesPartRouteProcess);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件途程设定工序")
    @UserOperationLog("保存料件途程设定工序")
    public ResponseMessage<MesPartRouteProcess> save(@RequestBody MesPartRouteProcess mesPartRouteProcess){
        ValidatorUtil.validateEntity(mesPartRouteProcess, AddGroup.class);
        mesPartRouteProcess.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesPartRouteProcessService.save(mesPartRouteProcess));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件途程设定工序")
    @UserOperationLog("更新料件途程设定工序")
    public ResponseMessage<MesPartRouteProcess> update(@RequestBody MesPartRouteProcess mesPartRouteProcess){
        ValidatorUtil.validateEntity(mesPartRouteProcess, UpdateGroup.class);
        MesPartRouteProcess mesPartRouteProcessOld = mesPartRouteProcessService.findById(mesPartRouteProcess.getId()).orElse(null);
        if(mesPartRouteProcessOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesPartRouteProcess,mesPartRouteProcessOld);
        return ResponseMessage.ok(mesPartRouteProcessService.save(mesPartRouteProcessOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件途程设定工序")
    @UserOperationLog("删除料件途程设定工序")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesPartRouteProcessService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}