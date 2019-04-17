package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.base.service.BaseInstructionService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.service.BaseItemsTargetService;
import com.m2micro.m2mfa.base.vo.BaseInstructionQueryObj;
import com.m2micro.m2mfa.common.util.FileUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
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
import io.swagger.annotations.ApiOperation;
import com.m2micro.m2mfa.base.entity.BaseInstruction;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作业指导书 前端控制器
 * @author chengshuhong
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/base/baseInstruction")
@Api(value="作业指导书 前端控制器")
@Authorize
public class BaseInstructionController {

    @Autowired
    BaseInstructionService baseInstructionService;
    @Autowired
    BaseItemsTargetService baseItemsTargetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="作业指导书列表")
    @UserOperationLog("作业指导书列表")
    public ResponseMessage<PageUtil<BaseInstruction>> list(BaseInstructionQueryObj query){
        PageUtil<BaseInstruction> page = baseInstructionService.list(query);
        return ResponseMessage.ok(page);
    }


    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="作业指导书详情")
    @UserOperationLog("作业指导书详情")
    public ResponseMessage<BaseInstruction> info(@PathVariable("id") String id){
        BaseInstruction baseInstruction = baseInstructionService.findById(id).orElse(null);
        baseInstruction.setCategoryName(baseItemsTargetService.findById(baseInstruction.getCategory()).orElse(null).getItemName());
        return ResponseMessage.ok(baseInstruction);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存作业指导书")
    @UserOperationLog("保存作业指导书")
    public ResponseMessage save( BaseInstruction baseInstruction, MultipartFile file){
        baseInstructionService.save(baseInstruction,file);
        return ResponseMessage.ok();
    }


    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新作业指导书")
    @UserOperationLog("更新作业指导书")
    public ResponseMessage update(BaseInstruction baseInstruction,  @RequestParam(required = false) MultipartFile file){
        baseInstructionService.update(baseInstruction,file);
        return ResponseMessage.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除作业指导书")
    @UserOperationLog("删除作业指导书")
    public ResponseMessage delete(@RequestBody String[] ids){

        return baseInstructionService.delete(ids);
    }

    /**
     * 下载
     */
    @RequestMapping("/download")
    @ApiOperation(value="下载这也指导书")
    @UserOperationLog("下载这也指导书")
    public void download( String id, HttpServletResponse response)throws IOException {
        BaseInstruction baseInstruction = baseInstructionService.findById(id).orElse(null);
        if(baseInstruction.equals(null)){
            throw  new MMException("该文件路径不存在。");
        }
        FileUtil.downloadFile(baseInstruction.getInstructionCode()+"-"+baseInstruction.getRevsion(),baseInstruction.getFileUrl(),response);
    }

    /**
     * 添加基本信息
     */
    @RequestMapping("/addDetails")
    @ApiOperation(value="添加基本信息")
    @UserOperationLog("添加基本信息")
    public ResponseMessage<Map> delete(){
        Map map = new HashMap();
        TreeNode instructioncategory = baseItemsTargetService.getTreeNode("Instruction_Category ");
        map.put("instructioncategory",instructioncategory );
        return ResponseMessage.ok(map);
    }


}
