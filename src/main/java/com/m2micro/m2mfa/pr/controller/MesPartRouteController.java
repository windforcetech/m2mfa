package com.m2micro.m2mfa.pr.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.base.service.BaseRouteDefService;
import com.m2micro.m2mfa.base.service.BaseRouteDescService;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.query.MesPartRouteQuery;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private BaseRouteDescService  baseRouteDescService;
    @Autowired
    private BaseRouteDefService baseRouteDefService;
    @Autowired
    private BasePartsService basePartsService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation(value="料件途程设定主档列表")
    @UserOperationLog("料件途程设定主档列表")
    public ResponseMessage<PageUtil<MesPartRoute>> list(@RequestBody  MesPartRouteQuery query){
        PageUtil<MesPartRoute> page = mesPartRouteService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @PostMapping("/info")
    @ApiOperation(value="料件途程设定主档详情")
    @UserOperationLog("料件途程设定主档详情")
    public ResponseMessage<MesPartvo> info(@ApiParam(value = "partRouteId",required=true) @RequestParam(required = true)   String  partRouteId){
        MesPartvo mesPartRoute = mesPartRouteService.info(partRouteId);
        return ResponseMessage.ok(mesPartRoute);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value="保存料件途程设定主档")
    @UserOperationLog("保存料件途程设定主档")
    public ResponseMessage<String > save(@RequestBody MesPartvo mesPartRoutevo){
        return  mesPartRouteService.save(mesPartRoutevo.getMesPartRoute(),mesPartRoutevo.getMesPartRouteProcesss(),mesPartRoutevo.getMesPartRouteStations())==true ? ResponseMessage.ok():ResponseMessage.error("添加失败。");
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value="更新料件途程设定主档")
    @UserOperationLog("更新料件途程设定主档")
    public ResponseMessage update(@RequestBody MesPartvo mesPartRoutevo){

         return    mesPartRouteService.update(mesPartRoutevo.getMesPartRoute(),mesPartRoutevo.getMesPartRouteProcesss(),mesPartRoutevo.getMesPartRouteStations())==true ? ResponseMessage.ok():ResponseMessage.error("更新失败。");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value="删除料件途程设定主档")
    @UserOperationLog("删除料件途程设定主档")
    public ResponseMessage delete(@RequestBody String[] ids){
        String msgs="";
        for(int i =0;i<ids.length;i++){
            String  msg= mesPartRouteService.delete(ids[i]);
            if(!msg.trim().equals("")){
                msgs+=msg;
            }
        }
        ResponseMessage rm = ResponseMessage.ok();
        if(msgs.trim()!=""){
            rm.setMessage(msgs.trim()+"该料件途程已经安排生产，不允许删除。");
        }
        return  rm;

    }

    @PostMapping("/addDetails")
    @ApiOperation(value="添加基本信息")
    @UserOperationLog("途程添加基本信息")
    public ResponseMessage addDetails(@ApiParam(value = "routId",required=true) @RequestParam(required = true) String routId){
        Map map = new HashMap();
        List<BaseProcessStation> baseProcessStations = baseRouteDescService.findbaseProcessStations(routId);
        List<BaseRouteDef> routeDefs = baseRouteDefService.findroutedef(routId);
        List<BaseParts> baseparts= basePartsService.findAll();
        List<MesPartRoute> mesPartRoutes =mesPartRouteService.findAll();
        for(int i=0;i<baseparts.size();i++ ){
            for(MesPartRoute m: mesPartRoutes){
                if(m.getPartId().equals(baseparts.get(i).getPartId())){
                    baseparts.remove(i);
                }
            }
        }

        map.put("routeDefs",routeDefs);
        map.put("baseProcessStations",baseProcessStations);
        map.put("baseparts",baseparts);
        return ResponseMessage.ok(map);
    }

}
