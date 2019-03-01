package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.query.BaseMoldQuery;
import com.m2micro.m2mfa.base.repository.BaseItemsTargetRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.service.BaseMoldService;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseMold;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模具主档 前端控制器
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping("/base/baseMold")
@Api(value="模具主档 前端控制器")
@Authorize
public class BaseMoldController {
    @Autowired
    BaseMoldService baseMoldService;

    @Autowired
    BaseItemsTargetService baseItemsTargetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="模具主档列表")
    @UserOperationLog("模具主档列表")
    public ResponseMessage<PageUtil<BaseMold>> list(BaseMoldQuery query){
        PageUtil<BaseMold> page = baseMoldService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="模具主档详情")
    @UserOperationLog("模具主档详情")
    public ResponseMessage<Map> info(@PathVariable("id") String id){
        BaseMold baseMold = baseMoldService.findById(id).orElse(null);
        Map map = new HashMap();
        List<SelectNode> moldState = baseItemsTargetService.getSelectNode("mold_state");
        List<SelectNode> moldPlacementoldPlacement = baseItemsTargetService.getSelectNode("mold_placement");
        List<SelectNode> moldType = baseItemsTargetService.getSelectNode("mold_type");
        List<SelectNode> moldStructure = baseItemsTargetService.getSelectNode("mold_structure");
        List<SelectNode> materialType = baseItemsTargetService.getSelectNode("material_type");
        TreeNode moldCategory = baseItemsTargetService.getTreeNode("mold_category");

        map.put("moldState",moldState);
        map.put("moldCategory",moldCategory);
        map.put("moldPlacementoldPlacement",moldPlacementoldPlacement);
        map.put("moldType",moldType);
        map.put("moldStructure",moldStructure);
        map.put("materialType",materialType);
        map.put("baseMold",baseMold);
        return ResponseMessage.ok(map);

    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存模具主档")
    @UserOperationLog("保存模具主档")
    public ResponseMessage<BaseMold> save(@RequestBody BaseMold baseMold){
        ValidatorUtil.validateEntity(baseMold, AddGroup.class);
        baseMold.setMoldId(UUIDUtil.getUUID());
        //校验code唯一性
        List<BaseMold> list = baseMoldService.findAllByCode(baseMold.getCode());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        return ResponseMessage.ok(baseMoldService.save(baseMold));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新模具主档")
    @UserOperationLog("更新模具主档")
    public ResponseMessage<BaseMold> update(@RequestBody BaseMold baseMold){
        ValidatorUtil.validateEntity(baseMold, UpdateGroup.class);

        BaseMold baseMoldOld = baseMoldService.findById(baseMold.getMoldId()).orElse(null);
        if(baseMoldOld==null){
            throw new MMException("数据库不存在该记录");
        }
        //校验code唯一性
        List<BaseMold> list = baseMoldService.findByCodeAndMoldIdNot(baseMold.getCode(),baseMold.getMoldId());
        if(list!=null&&list.size()>0){
            throw new MMException("编号不唯一！");
        }
        PropertyUtil.copy(baseMold,baseMoldOld);
        return ResponseMessage.ok(baseMoldService.save(baseMoldOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除模具主档")
    @UserOperationLog("删除模具主档")
    public ResponseMessage delete(@RequestBody  String[] ids){


        return  baseMoldService.delete(ids);
    }

    @RequestMapping("/addDetails")
    @ApiOperation(value="获取模具主档添加基本信息")
    @UserOperationLog("获取模具主档添加基本信息")
    public ResponseMessage addDetails(){
        Map map = new HashMap();
        List<SelectNode> moldState = baseItemsTargetService.getSelectNode("mold_state");
        List<SelectNode> moldPlacementoldPlacement = baseItemsTargetService.getSelectNode("mold_placement");
        List<SelectNode> moldType = baseItemsTargetService.getSelectNode("mold_type");
        List<SelectNode> moldStructure = baseItemsTargetService.getSelectNode("mold_structure");
        List<SelectNode> materialType = baseItemsTargetService.getSelectNode("material_type");
        TreeNode moldCategory = baseItemsTargetService.getTreeNode("mold_category");

        map.put("moldState",moldState);
        map.put("moldCategory",moldCategory);
        map.put("moldPlacementoldPlacement",moldPlacementoldPlacement);
        map.put("moldType",moldType);
        map.put("moldStructure",moldStructure);
        map.put("materialType",materialType);
        return ResponseMessage.ok(map);
    }


    /**
     * 排产单需要的模具 列表
     */
    @PostMapping("/mesMoScheduleMolds")
    @ApiOperation(value="排产单需要的模具列表")
    @UserOperationLog("排产单需要的模具列表")
    public ResponseMessage<List<BaseMold>> mesMoScheduleMolds(){
      List<BaseMold>molds = baseMoldService.findbyisMold();
      if(molds.isEmpty()){
          throw  new MMException("模具信息不存在。");
      }
        return ResponseMessage.ok(molds);
    }


}
