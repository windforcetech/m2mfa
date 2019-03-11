package com.m2micro.m2mfa.base.controller;

import com.google.common.collect.Lists;
import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseBomDescQuery;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.base.vo.*;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 员工排班表 前端控制器
 *
 * @author liaotao
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/base/baseBomDesc")
@Api(description = "Bom 前端控制器")
@Authorize
public class BaseBomDescController {
    @Autowired
    BaseBomDescService baseBomDescService;
    @Autowired
    BaseBomDefService baseBomDefService;
    @Autowired
    BaseBomSubstituteService baseBomSubstituteService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "bom列表")
    @UserOperationLog("bom列表")
    public ResponseMessage<PageUtil<BaseBomDesc>> list(BaseBomDescQuery query) {

        QBaseBomDesc baseBomDesc = QBaseBomDesc.baseBomDesc;
        BooleanBuilder expression = new BooleanBuilder();
        if (StringUtils.isNotEmpty(query.getPartId())) {
            expression.and(baseBomDesc.partId.like("%" + query.getPartId() + "%"));
        }
        ArrayList<BaseBomDesc> baseBomDescs = Lists.newArrayList(baseBomDescService.findAll(expression));

        PageUtil<BaseBomDesc> of = PageUtil.of(baseBomDescs, baseBomDescs.size(), query.getSize(), query.getPage());
        return ResponseMessage.ok(of);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存Bom")
    @UserOperationLog("保存Bom")
    public ResponseMessage<List<BaseBomDef>> save(@RequestBody BaseBomDesc staffShiftObj) throws ParseException {
        ValidatorUtil.validateEntity(staffShiftObj, AddGroup.class);
        QBaseBomDesc baseBomDesc = QBaseBomDesc.baseBomDesc;
        BooleanExpression expression = baseBomDesc.category.eq(staffShiftObj.getCategory())
                .and(baseBomDesc.partId.eq(staffShiftObj.getPartId()))
                .and(baseBomDesc.version.eq(staffShiftObj.getVersion()))
                .and(baseBomDesc.enabled.eq(true));
        Iterable<BaseBomDesc> all = baseBomDescService.findAll(expression);
        ArrayList<BaseBomDesc> baseBomDescs = Lists.newArrayList(all);
        if (baseBomDescs.size() > 0)
            throw new MMException("料号、类型、版本唯一");
        staffShiftObj.setBomId(UUIDUtil.getUUID());
        BaseBomDesc save = baseBomDescService.save(staffShiftObj);
        List<BaseBomDef> bomDefObjList = staffShiftObj.getBomDefObjList();
        bomDefObjList.forEach(x -> {
            x.setBomId(save.getBomId());
            x.setId(UUIDUtil.getUUID());
        });

        return ResponseMessage.ok(baseBomDefService.saveAll(bomDefObjList));
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新bom表")
    @UserOperationLog("更新bom表")
    @Transactional
    public ResponseMessage<BaseStaffshift> update(@RequestBody BaseBomDesc baseBomDescObj) {
        ValidatorUtil.validateEntity(baseBomDescObj, UpdateGroup.class);
        BaseBomDesc baseStaffshiftOld = baseBomDescService.findById(baseBomDescObj.getBomId()).orElse(null);
        if (baseStaffshiftOld == null) {
            throw new MMException("数据库不存在BomDesc记录");
        }

        QBaseBomDesc baseBomDesc = QBaseBomDesc.baseBomDesc;
        BooleanExpression expression = baseBomDesc.category.eq(baseBomDescObj.getCategory())
                .and(baseBomDesc.partId.eq(baseBomDescObj.getPartId()))
                .and(baseBomDesc.version.eq(baseBomDescObj.getVersion()))
                .and(baseBomDesc.bomId.notEqualsIgnoreCase(baseBomDescObj.getBomId()))
                .and(baseBomDesc.enabled.eq(true));
        Iterable<BaseBomDesc> all = baseBomDescService.findAll(expression);
        ArrayList<BaseBomDesc> baseBomDescs = Lists.newArrayList(all);
        if (baseBomDescs.size() > 0)
            throw new MMException("料号、类型、版本唯一");

        PropertyUtil.copy(baseBomDescObj, baseStaffshiftOld);
        baseBomDescService.save(baseStaffshiftOld);

        baseBomDescObj.getBomDefObjList().forEach(x -> {

            if (StringUtils.isEmpty(x.getId())) {//为null,新增
                x.setBomId(baseBomDescObj.getBomId());
                x.setId(UUIDUtil.getUUID());
                baseBomDefService.save(x);
            } else {//修改
                BaseBomDef baseBomDefOld = baseBomDefService.findById(x.getId()).orElse(null);
                if (baseBomDefOld == null) {
                    throw new MMException("数据库不存在BomDef记录");
                }
                PropertyUtil.copy(x, baseBomDefOld);
                baseBomDefService.save(baseBomDefOld);
            }

        });
        return ResponseMessage.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/deletedesc")
    @ApiOperation(value = "删除bom desc表")
    @UserOperationLog("删除bom desc表")
    @Transactional
    public ResponseMessage deletedesc(@RequestBody String[] ids) {


//        QBaseBomDef baseBomDesc = QBaseBomDef.baseBomDef;
//        BooleanExpression expression = baseBomDesc.bomId.in(ids);
//        Iterable<BaseBomDef> all = baseBomDefService.findAll(expression);
//        baseBomDefService.deleteAll(all);
//        baseBomDescService.deleteByIds(ids);
        baseBomDefService.deleteByBomIds(Arrays.asList(ids));
        baseBomDescService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/deletedef")
    @ApiOperation(value = "删除bom def表")
    @UserOperationLog("删除bom def表")
    @Transactional
    public ResponseMessage deletedef(@RequestBody String[] ids) {
        baseBomDefService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 保存
     */
    @PostMapping("/ecn")
    @ApiOperation(value = "工程变更号码")
    @UserOperationLog("工程变更号码")
    public ResponseMessage<List<BaseBomDef>> ecn(@RequestBody BaseBomDesc staffShiftObj) throws ParseException {
        ValidatorUtil.validateEntity(staffShiftObj, AddGroup.class);

        BaseBomDesc baseStaffshiftOld = baseBomDescService.findById(staffShiftObj.getBomId()).orElse(null);
        if (baseStaffshiftOld == null) {
            throw new MMException("数据库不存在BomDesc记录");
        }

        baseStaffshiftOld.setEnabled(false);
        baseBomDescService.save(baseStaffshiftOld);//设置无效

        staffShiftObj.setVersion(baseStaffshiftOld.getVersion() + 1);
        staffShiftObj.setBomId(UUIDUtil.getUUID());
        BaseBomDesc save = baseBomDescService.save(staffShiftObj);
        List<BaseBomDef> bomDefObjList = staffShiftObj.getBomDefObjList();
        bomDefObjList.forEach(x -> {
            x.setBomId(save.getBomId());
            x.setId(UUIDUtil.getUUID());
        });

        return ResponseMessage.ok(baseBomDefService.saveAll(bomDefObjList));
    }

    /**
     * 更新
     */
    @PostMapping("/maintain")
    @ApiOperation(value = "替代维护")
    @UserOperationLog("替代维护")
    @Transactional
    public ResponseMessage<BaseStaffshift> maintain(@RequestBody BaseBomDesc baseBomDescObj) {
        ValidatorUtil.validateEntity(baseBomDescObj, UpdateGroup.class);
        BaseBomDesc baseStaffshiftOld = baseBomDescService.findById(baseBomDescObj.getBomId()).orElse(null);
        if (baseStaffshiftOld == null) {
            throw new MMException("数据库不存在BomDesc记录");
        }
        baseBomDescObj.getBomSubstituteList().forEach(x -> {

            if (StringUtils.isEmpty(x.getId())) {//为null,新增
                x.setBomId(baseBomDescObj.getBomId());
                x.setId(UUIDUtil.getUUID());
                baseBomSubstituteService.save(x);
            } else {//修改
                BaseBomSubstitute baseBomSubstitute = baseBomSubstituteService.findById(x.getId()).orElse(null);
                if (baseBomSubstitute == null) {
                    throw new MMException("数据库不存在BomSubtitute记录");
                }
                PropertyUtil.copy(x, baseBomSubstitute);
                baseBomSubstituteService.save(baseBomSubstitute);
            }

        });
        return ResponseMessage.ok();
    }
}