package com.m2micro.m2mfa.pr.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.pr.service.MesPartRouteStationService;
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
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件途程设定工位 前端控制器
 * @author chenshuhong
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/pr/mesPartRouteStation")
@Api(value="料件途程设定工位 前端控制器")
@Authorize
public class MesPartRouteStationController {
    @Autowired
    MesPartRouteStationService mesPartRouteStationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件途程设定工位列表")
    @UserOperationLog("料件途程设定工位列表")
    public ResponseMessage<PageUtil<MesPartRouteStation>> list(Query query){
        PageUtil<MesPartRouteStation> page = mesPartRouteStationService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件途程设定工位详情")
    @UserOperationLog("料件途程设定工位详情")
    public ResponseMessage<MesPartRouteStation> info(@PathVariable("id") String id){
        MesPartRouteStation mesPartRouteStation = mesPartRouteStationService.findById(id).orElse(null);
        return ResponseMessage.ok(mesPartRouteStation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件途程设定工位")
    @UserOperationLog("保存料件途程设定工位")
    public ResponseMessage<MesPartRouteStation> save(@RequestBody MesPartRouteStation mesPartRouteStation){
        ValidatorUtil.validateEntity(mesPartRouteStation, AddGroup.class);
        mesPartRouteStation.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesPartRouteStationService.save(mesPartRouteStation));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件途程设定工位")
    @UserOperationLog("更新料件途程设定工位")
    public ResponseMessage<MesPartRouteStation> update(@RequestBody MesPartRouteStation mesPartRouteStation){
        ValidatorUtil.validateEntity(mesPartRouteStation, UpdateGroup.class);
        MesPartRouteStation mesPartRouteStationOld = mesPartRouteStationService.findById(mesPartRouteStation.getId()).orElse(null);
        if(mesPartRouteStationOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesPartRouteStation,mesPartRouteStationOld);
        return ResponseMessage.ok(mesPartRouteStationService.save(mesPartRouteStationOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件途程设定工位")
    @UserOperationLog("删除料件途程设定工位")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesPartRouteStationService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

}