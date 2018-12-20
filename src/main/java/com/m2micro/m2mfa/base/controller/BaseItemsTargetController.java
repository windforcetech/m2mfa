package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

/**
 * 参考资料对应表 前端控制器
 * @author liaotao
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/base/baseItemsTarget")
@Api(value="参考资料对应表 前端控制器")
@Authorize
public class BaseItemsTargetController {
    @Autowired
    BaseItemsTargetService baseItemsTargetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="参考资料对应表列表")
    @UserOperationLog("参考资料对应表列表")
    public ResponseMessage<PageUtil<BaseItemsTarget>> list(Query query){
        PageUtil<BaseItemsTarget> page = baseItemsTargetService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="参考资料对应表详情")
    @UserOperationLog("参考资料对应表详情")
    public ResponseMessage<BaseItemsTarget> info(@PathVariable("id") String id){
        BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(id).orElse(null);
        return ResponseMessage.ok(baseItemsTarget);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存参考资料对应表")
    @UserOperationLog("保存参考资料对应表")
    public ResponseMessage<BaseItemsTarget> save(@RequestBody BaseItemsTarget baseItemsTarget){
        //ValidatorUtil.validateEntity(baseItemsTarget, AddGroup.class);
        baseItemsTarget.setId(UUIDUtil.getUUID());
        return ResponseMessage.ok(baseItemsTargetService.save(baseItemsTarget));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新参考资料对应表")
    @UserOperationLog("更新参考资料对应表")
    public ResponseMessage<BaseItemsTarget> update(@RequestBody BaseItemsTarget baseItemsTarget){
        ValidatorUtil.validateEntity(baseItemsTarget, UpdateGroup.class);
        BaseItemsTarget baseItemsTargetOld = baseItemsTargetService.findById(baseItemsTarget.getId()).orElse(null);
        if(baseItemsTargetOld==null){
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseItemsTarget,baseItemsTargetOld);
        return ResponseMessage.ok(baseItemsTargetService.save(baseItemsTargetOld));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除参考资料对应表")
    @UserOperationLog("删除参考资料对应表")
    public ResponseMessage delete(@RequestBody String[] ids){
        baseItemsTargetService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 获取所有的资料信息
     */
    @RequestMapping("/getAllItemsTarget")
    @ApiOperation(value="获取所有参考资料")
    @UserOperationLog("获取所有参考资料")
    public ResponseMessage getAllItemsTarget(@ApiParam("名称")String itemName){
        List<BaseItemsTarget> list = baseItemsTargetService.getAllItemsTarget(itemName);
        String[] itemNames = list.stream().map(BaseItemsTarget::getItemName).toArray(String[]::new);
        return ResponseMessage.ok(itemNames);
    }

    @RequestMapping("/getTreeNode")
    @ApiOperation(value="获取所有参考资料")
    @UserOperationLog("获取所有参考资料")
    public ResponseMessage<TreeNode> getTreeNode(@RequestParam("itemCode")@ApiParam("名称")String itemCode){
        return ResponseMessage.ok(baseItemsTargetService.getTreeNode(itemCode));
    }

    @RequestMapping("/getSelectNode")
    @ApiOperation(value="获取所有参考资料")
    @UserOperationLog("获取所有参考资料")
    public ResponseMessage<List<SelectNode>> getSelectNode(@RequestParam("itemCode")@ApiParam("名称")String itemCode){
        return ResponseMessage.ok(baseItemsTargetService.getSelectNode(itemCode));
    }

}