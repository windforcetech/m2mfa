package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import com.m2micro.m2mfa.base.entity.BaseItemsTarget;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.repository.BaseInstructionRepository;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return ResponseMessage.ok(baseItemsTargetService.saveEntity(baseItemsTarget));
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新参考资料对应表")
    @UserOperationLog("更新参考资料对应表")
    public ResponseMessage<BaseItemsTarget> update(@RequestBody BaseItemsTarget baseItemsTarget){
        return ResponseMessage.ok(baseItemsTargetService.updateEntity(baseItemsTarget));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除参考资料对应表")
    @UserOperationLog("删除参考资料对应表")
    @Transactional
    public ResponseMessage delete(@RequestBody String[] ids){

        return baseItemsTargetService.delete(ids);
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
