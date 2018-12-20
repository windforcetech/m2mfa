package com.m2micro.m2mfa.pr.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
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
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import org.springframework.web.bind.annotation.RestController;

/**
 * 料件途程设定主档 前端控制器
 * @author liaotao
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/pr/mesPartRoute")
@Api(value="料件途程设定主档 前端控制器")
@Authorize
public class MesPartRouteController {
    @Autowired
    MesPartRouteService mesPartRouteService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件途程设定主档列表")
    @UserOperationLog("料件途程设定主档列表")
    public ResponseMessage<PageUtil<MesPartRoute>> list(Query query){
        PageUtil<MesPartRoute> page = mesPartRouteService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件途程设定主档详情")
    @UserOperationLog("料件途程设定主档详情")
    public ResponseMessage<MesPartRoute> info(@PathVariable("id") String id){
        MesPartRoute mesPartRoute = mesPartRouteService.findById(id).orElse(null);
        return ResponseMessage.ok(mesPartRoute);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件途程设定主档")
    @UserOperationLog("保存料件途程设定主档")
    public ResponseMessage<MesPartRoute> save(@RequestBody MesPartRoute mesPartRoute){
        ValidatorUtil.validateEntity(mesPartRoute, AddGroup.class);
        mesPartRoute.setPartRouteId(UUIDUtil.getUUID());
        return ResponseMessage.ok(mesPartRouteService.save(mesPartRoute));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件途程设定主档")
    @UserOperationLog("更新料件途程设定主档")
    public ResponseMessage<MesPartRoute> update(@RequestBody MesPartRoute mesPartRoute){
        ValidatorUtil.validateEntity(mesPartRoute, UpdateGroup.class);
        MesPartRoute mesPartRouteOld = mesPartRouteService.findById(mesPartRoute.getPartRouteId()).orElse(null);
        if(mesPartRouteOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(mesPartRoute,mesPartRouteOld);
        return ResponseMessage.ok(mesPartRouteService.save(mesPartRouteOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件途程设定主档")
    @UserOperationLog("删除料件途程设定主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        mesPartRouteService.deleteByIds(ids);
        return ResponseMessage.ok();
    }
/*
    这两个是返回给前端然后进行选择后添加到关联表的选择
    SELECT  * from base_route_desc m
    INNER JOIN  base_route_def  r    ON r.route_id=m.route_id
    WHERE m.route_id='481E498831274406987045502FF3E36C'


     这是工序跟工艺的关联
      SELECT  * from base_route_desc    m
			inner join  base_route_def      l   on l.route_id=m.route_id
      inner join base_process_station  p  on  l.process_id=p.process_id
      where m.route_id='481e498831274406987045502ff3e36c'
 */
}