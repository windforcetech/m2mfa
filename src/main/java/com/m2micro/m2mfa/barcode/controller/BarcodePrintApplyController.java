package com.m2micro.m2mfa.barcode.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.barcode.entity.BarcodePrintApply;
import com.m2micro.m2mfa.barcode.query.BarcodeQuery;
import com.m2micro.m2mfa.barcode.query.PrintApplyQuery;
import com.m2micro.m2mfa.barcode.query.ScheduleQuery;
import com.m2micro.m2mfa.barcode.repository.BarcodePrintResourcesRepository;
import com.m2micro.m2mfa.barcode.service.BarcodePrintApplyService;
import com.m2micro.m2mfa.barcode.vo.CheckObj;
import com.m2micro.m2mfa.barcode.vo.PrintApplyObj;
import com.m2micro.m2mfa.barcode.vo.PrintResourceObj;
import com.m2micro.m2mfa.barcode.vo.ScheduleObj;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签打印表单 前端控制器
 *
 * @author liaotao
 * @since 2019-03-27
 */
@RestController
@RequestMapping("/barcode/barcodePrintApply")
@Api(value = "标签打印表单 前端控制器", description = "打印申请接口")
@Authorize
public class BarcodePrintApplyController {

    @Autowired
    BarcodePrintApplyService barcodePrintApplyService;
    @Autowired
    BarcodePrintResourcesRepository barcodePrintResourcesRepository;

    @GetMapping("/Schedulelist")
    @ApiOperation(value = "模糊分页查询排产单")
    @UserOperationLog("模糊分页查询排产单")
    public ResponseMessage<PageUtil<ScheduleObj>> schedulelist(ScheduleQuery query) {
        PageUtil<ScheduleObj> list = barcodePrintApplyService.list(query);
        return ResponseMessage.ok(list);
    }


    @GetMapping("/ScheduleInfo/{id}")
    @ApiOperation(value = "通过排产单id 获取排产单详情")
    @UserOperationLog("通过排产单id 获取排产单详情")
    public ResponseMessage<ScheduleObj> scheduleInfo(@PathVariable("id") String scheduleId) {
        ScheduleObj scheduleObj = barcodePrintApplyService.scheduleInfo(scheduleId);
        return ResponseMessage.ok(scheduleObj);
    }


    /**
     * 添加打印申请
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加打印申请")
    @UserOperationLog("添加打印申请")
    public ResponseMessage<BarcodePrintApply> add(@RequestBody BarcodePrintApply barcodePrintApply) {
        ValidatorUtil.validateEntity(barcodePrintApply, AddGroup.class);
        return ResponseMessage.ok(barcodePrintApplyService.add(barcodePrintApply));
    }

    /**
     * 补打标签
     */
    @PostMapping("/makeupcode")
    @ApiOperation(value = "补打标签")
    @UserOperationLog("补打标签")
    public ResponseMessage<BarcodePrintApply> makeupcode(@RequestBody BarcodePrintApply barcodePrintApply) {
        barcodePrintApply.setPrintCategory("replenish");
        ValidatorUtil.validateEntity(barcodePrintApply, AddGroup.class);
        return ResponseMessage.ok(barcodePrintApplyService.add(barcodePrintApply));
    }
    /**
     * 查询单个
     */
    @RequestMapping("/info/{id}")
    @ApiOperation(value = "标签打印表单详情")
    @UserOperationLog("标签打印表单详情")
    public ResponseMessage<PrintApplyObj> info(@PathVariable("id") String id) {
        PrintApplyQuery query = new PrintApplyQuery();
        query.setApplyId(id);
        PageUtil<PrintApplyObj> page = barcodePrintApplyService.printApplyList(query);
        return ResponseMessage.ok(page.getData().get(0));
    }


    /**
     * 批量审核
     */
    @PostMapping("/checkList")
    @ApiOperation(value = "批量审核")
    @UserOperationLog("批量审核")
    public ResponseMessage checkList(@RequestBody String[] ids) {
        barcodePrintApplyService.checkList(ids);
        return ResponseMessage.ok();
    }


    @PostMapping("/deleteList")
    @ApiOperation(value = "批量删除打印申请")
    @UserOperationLog("批量删除打印申请")
    public ResponseMessage deleteList(@RequestBody String[] ids) {
        barcodePrintApplyService.deleteByIds(ids);
      for(String id :ids ){
          barcodePrintResourcesRepository.deleteByApplyId(id);
      }
        return ResponseMessage.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/printApplyList")
    @ApiOperation(value = "标签打印表单列表")
    @UserOperationLog("标签打印表单列表")
    public ResponseMessage<PageUtil<PrintApplyObj>> printApplyList(PrintApplyQuery query) {
        PageUtil<PrintApplyObj> page = barcodePrintApplyService.printApplyList(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 列表
     */
    @GetMapping("/printApplyListAfterCheckedOk")
    @ApiOperation(value = "标签打印表单审核过的列表")
    @UserOperationLog("标签打印表单审核过的列表")
    public ResponseMessage<PageUtil<PrintApplyObj>> printApplyListAfterCheckedOk(PrintApplyQuery query) {
        PageUtil<PrintApplyObj> page = barcodePrintApplyService.printApplyListAfterCheckedOk(query);
        return ResponseMessage.ok(page);
    }


    @RequestMapping("/printApplyDetail")
    @ApiOperation(value = "获取打印申请详情")
    @UserOperationLog("获取打印申请详情")
    public ResponseMessage< PageUtil<PrintResourceObj>> list(BarcodeQuery query) {
        PageUtil<PrintResourceObj>  printApplyObj = barcodePrintApplyService.printApplylist(query);
        return ResponseMessage.ok(printApplyObj);
    }


    @PostMapping("/getPrintApplyObj")
    @ApiOperation(value = "获取打印主档")
    @UserOperationLog("获取打印主档")
    public ResponseMessage< PrintApplyObj> getPrintApplyObj(String applyId) {
        PrintApplyObj printApplyObj = barcodePrintApplyService.getPrintApplyObj(applyId);
        return ResponseMessage.ok(printApplyObj);
    }


    @PostMapping("/printApplyGenerate")
    @ApiOperation(value = "打印条码生成")
    @UserOperationLog("打印条码生成")
    public ResponseMessage printApplyGenerate(String applyId, Integer num) {
      barcodePrintApplyService.generateLabel(applyId, num);
        return ResponseMessage.ok();
    }


    /**
     * 批量审核打印审核 打印作废，打印
     */
    @PostMapping("/printCheckList")
    @ApiOperation(value = "审核打印 :1, 已打印：2， 打印作废:-1,未审核：0")
    @UserOperationLog("审核打印")
    public ResponseMessage printCheckList(@RequestBody CheckObj checkObj) {
        barcodePrintApplyService.printCheckList(checkObj.getIds(),checkObj.getFlag());
        return ResponseMessage.ok();
    }


}
