package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseUnit;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.query.BasePartsQuery;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.base.service.BaseUnitService;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseParts;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 料件基本资料 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseParts")
@Api(value="料件基本资料 前端控制器")
@Authorize
public class BasePartsController {
    @Autowired
    BasePartsService basePartsService;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="料件基本资料列表")
    @UserOperationLog("料件基本资料列表")
    public ResponseMessage<PageUtil<BaseParts>> list(BasePartsQuery query){
        PageUtil<BaseParts> page = basePartsService.list(query);
        return ResponseMessage.ok(page);
    }


    /**
     * 列表
     */
    @RequestMapping("/barcodePartslist")
    @ApiOperation(value="料件基本资料列表（过滤编码打印）")
    @UserOperationLog("料件基本资料列表（过滤编码打印）")
    public ResponseMessage<PageUtil<BaseParts>> barcodePartslist(BasePartsQuery query){
            PageUtil<BaseParts> page = basePartsService.barcodePartslist(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/workOrderPartslist")
    @ApiOperation(value="料件基本资料列表（工单）")
    @UserOperationLog("料件基本资料列表（工单）")
    public ResponseMessage<PageUtil<BaseParts>> workOrderPartslist(BasePartsQuery query){
        PageUtil<BaseParts> page = basePartsService.workOrderPartslist(query);
        return ResponseMessage.ok(page);
    }
    /**
     * 列表
     */
    @RequestMapping("/listFilter")
    @ApiOperation(value="料件基本资料列表(过滤已关联的涂程)")
    @UserOperationLog("料件基本资料列表(过滤已关联的涂程)")
    public ResponseMessage<PageUtil<BaseParts>> listFilter(BasePartsQuery query){
        PageUtil<BaseParts> page = basePartsService.listFilter(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="料件基本资料详情")
    @UserOperationLog("料件基本资料详情")
    public ResponseMessage<Map> info(@PathVariable("id") String id){
        Map map = new HashMap();
        List<SelectNode> partsSource = baseItemsTargetService.getSelectNode("parts_source");
        map.put("partsSource",partsSource);
        map.put("baseUnitService",baseUnitService.list());
        BaseParts baseParts = basePartsService.findById(id).orElse(null);
        map.put("baseParts",baseParts);
        return ResponseMessage.ok(map);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存料件基本资料")
    @UserOperationLog("保存料件基本资料")
    public ResponseMessage<BaseParts> save(@RequestBody BaseParts baseParts){
        //设置groupId
        baseParts.setGroupId(TokenInfo.getUserGroupId());
        ValidatorUtil.validateEntity(baseParts, AddGroup.class);
        baseParts.setPartId(UUIDUtil.getUUID());
        //校验编号唯一性
        List<BaseParts> list = basePartsService.findByPartNoAndPartIdNot(baseParts.getPartNo(), "");
        if(list!=null&&list.size()>0){
            throw new MMException("料件编号不唯一！");
        }
        return ResponseMessage.ok(basePartsService.save(baseParts));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新料件基本资料")
    @UserOperationLog("更新料件基本资料")
    public ResponseMessage<BaseParts> update(@RequestBody BaseParts baseParts){
        //设置groupId
        baseParts.setGroupId(TokenInfo.getUserGroupId());
        ValidatorUtil.validateEntity(baseParts, UpdateGroup.class);
        BaseParts basePartsOld = basePartsService.findById(baseParts.getPartId()).orElse(null);
        if(basePartsOld==null){
           throw new MMException("数据库不存在该记录");
        }
        //校验编号唯一性
        List<BaseParts> list = basePartsService.findByPartNoAndPartIdNot(baseParts.getPartNo(), baseParts.getPartId());
        if(list!=null&&list.size()>0){
            throw new MMException("料件编号【"+baseParts.getPartNo()+"】已存在");
        }
        PropertyUtil.copy(baseParts,basePartsOld);
        return ResponseMessage.ok(basePartsService.save(basePartsOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除料件基本资料")
    @UserOperationLog("删除料件基本资料")
    public ResponseMessage delete(@RequestBody String[] ids){
        return basePartsService.deleteAllByIds(ids);
    }

    @RequestMapping("/addDetails")
    @ApiOperation(value="获取物料添加基本信息")
    @UserOperationLog("获取物料添加基本信息")
    public ResponseMessage addDetails(){
        Map map = new HashMap();
        List<SelectNode> partsSource = baseItemsTargetService.getSelectNode("parts_source");
        TreeNode partCategory = baseItemsTargetService.getTreeNode("part_category");
        map.put("partsSource",partsSource);
        map.put("partCategory",partCategory);
        map.put("baseUnitService",baseUnitService.list());
        return ResponseMessage.ok(map);
    }


    @RequestMapping("/selectpartNo")
    @ApiOperation(value="料件编号查询  ")
    @UserOperationLog("料件编号查询 ")
    public ResponseMessage<BaseParts> selectpartNo(@ApiParam(value = "partNo",required=true) @RequestParam(required = true)  String partNo){

        return ResponseMessage.ok(basePartsService.selectpartNo(partNo));
    }


    /**
     * 列表
     */
    @RequestMapping("/listNotUsed")
    @ApiOperation(value="获取未使用的料件基本资料列表")
    @UserOperationLog("获取未使用的料件基本资料列表")
    public ResponseMessage<PageUtil<BaseParts>> listNotUsed(BasePartsQuery query){
        PageUtil<BaseParts> page = basePartsService.findByNotUsedForPack(query);
        return ResponseMessage.ok(page);
    }
}
