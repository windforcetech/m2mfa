package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BasePartInstruction;
import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.m2mfa.base.query.BasePartInstructionQuery;
import com.m2micro.m2mfa.base.repository.BasePartInstructionRepository;
import com.m2micro.m2mfa.base.service.BasePartInstructionService;
import com.m2micro.m2mfa.base.service.BasePartsService;
import com.m2micro.m2mfa.base.vo.BasePartInstructionModel;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.m2mfa.pr.service.MesPartRouteService;
import com.m2micro.m2mfa.pr.vo.MesPartvo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作业指导书关联 前端控制器
 * @author chengshuhong
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/base/basePartInstruction")
@Api(value="作业指导书关联 前端控制器")
@Authorize
public class BasePartInstructionController {
    @Autowired
    BasePartInstructionService basePartInstructionService;
    @Autowired
    MesPartRouteService mesPartRouteService;
    @Autowired
    BasePartsService basePartsService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @ApiOperation(value="作业指导书关联列表")
    @UserOperationLog("作业指导书关联列表")
    public ResponseMessage<PageUtil<BasePartInstructionModel>> list(BasePartInstructionQuery query){
        PageUtil<BasePartInstructionModel> page = basePartInstructionService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value="作业指导书关联详情")
    @UserOperationLog("作业指导书关联详情")
    public ResponseMessage<List<BasePartInstructionModel>> info(@PathVariable("id") String id){

        return ResponseMessage.ok(basePartInstructionService.info(id));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ApiOperation(value="保存作业指导书关联")
    @UserOperationLog("保存作业指导书关联")
    @Transactional
    public ResponseMessage save(@RequestBody List< BasePartInstruction>  basePartInstructions){

        for(BasePartInstruction basePartInstruction :basePartInstructions){
            ValidatorUtil.validateEntity(basePartInstruction, AddGroup.class);
            basePartInstruction.setId(UUIDUtil.getUUID());
            basePartInstructionService.save(basePartInstruction);
        }

        return ResponseMessage.ok();
    }

    /**
     * 更新
     */
    @RequestMapping("/update")
    @ApiOperation(value="更新作业指导书关联")
    @UserOperationLog("更新作业指导书关联")
    @Transactional
    public ResponseMessage<BasePartInstruction> update(@RequestBody List< BasePartInstruction>  basePartInstructions){
        for(BasePartInstruction basePartInstruction :basePartInstructions){

            basePartInstructionService.save(basePartInstruction);
        }
        return ResponseMessage.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ApiOperation(value="删除作业指导书关联")
    @UserOperationLog("删除作业指导书关联")
    public ResponseMessage delete(@RequestBody String[] ids){
        basePartInstructionService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 添加基本信息
     */
    @RequestMapping("/addDetails")
    @ApiOperation(value="添加基本信息")
    @UserOperationLog("添加基本信息")
    public ResponseMessage<Map> delete(String partid){
        Map map = new HashMap();
        MesPartvo mesPartvos =mesPartRouteService.findparId(partid);
        BaseParts baseParts = basePartsService.findById(partid).orElse(null);
        map.put("baseParts",baseParts );
        if(mesPartvos !=null && mesPartvos.getMesPartRouteStations() !=null){
            map.put("mesPartRouteStations",mesPartvos.getMesPartRouteStations() );
        }else {
            map.put("mesPartRouteStations",null );
        }

        return ResponseMessage.ok(map);
    }


}
