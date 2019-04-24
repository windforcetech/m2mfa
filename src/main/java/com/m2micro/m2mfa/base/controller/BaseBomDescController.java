package com.m2micro.m2mfa.base.controller;

import com.google.common.collect.Lists;
import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.authorization.TokenInfo;
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

import java.math.BigDecimal;
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
    @Autowired
    BasePartsService basePartsService;

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
        List<BaseBomDesc> baseBomDescs = Lists.newArrayList(baseBomDescService.findAll(expression));

        baseBomDescs.forEach(baseBomDesc1 -> {
            BaseParts baseParts = basePartsService.selectpartNo(baseBomDesc1.getPartId());
            baseBomDesc1.setName(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getName());
            baseBomDesc1.setSpec(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSpec());
            baseBomDesc1.setProductionUnit(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getProductionUnit());

            List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc1.getBomId());
            allByBomId.forEach(baseBomDef -> {
                BaseParts baseParts1 = basePartsService.selectpartNo(baseBomDef.getPartId());
                baseBomDef.setName(baseParts1 == null ? "空指针错误(物料不存在)" : baseParts1.getName());
            });

            List<BaseBomSubstitute> baseBomSubstituteList = baseBomSubstituteService.findAllByBomId(baseBomDesc1.getBomId());
            baseBomDesc1.setBomSubstituteList(baseBomSubstituteList);

            baseBomDesc1.setBomDefObjList(allByBomId);
        });
        PageUtil<BaseBomDesc> of = PageUtil.of(baseBomDescs, baseBomDescs.size(), query.getSize(), query.getPage());
        return ResponseMessage.ok(of);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存Bom")
    @UserOperationLog("保存Bom")
    @Transactional
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

    /**
     * 删除替代维护
     */
    @PostMapping("/deletesubstitute")
    @ApiOperation(value = "删除bom 替代维护表")
    @UserOperationLog("删除bom 替代维护表")
    @Transactional
    public ResponseMessage deletesubstitute(@RequestBody String[] ids) {
        baseBomSubstituteService.deleteByIds(ids);
        return ResponseMessage.ok();
    }

    /**
     * 审核bom
     */
    @PostMapping("/check")
    @ApiOperation(value = "审核bom")
    @UserOperationLog("审核bom")
    public ResponseMessage check(String id) {
        BaseBomDesc baseBomDesc = baseBomDescService.findById(id).orElse(null);
        if (baseBomDesc == null)
            return ResponseMessage.error("不存在记录");
        baseBomDesc.setCheckFlag(true);
        baseBomDesc.setCheckOn(new Date());
        baseBomDesc.setCheckBy(TokenInfo.getTokenInfo(null).getUserID());
        baseBomDescService.updateById(id, baseBomDesc);
        return ResponseMessage.ok();
    }

    /**
     * 展示BOM
     */
    @GetMapping("/showbom")
    @ApiOperation(value = "展示bom")
    @UserOperationLog("展示bom")
    public ResponseMessage showbom(String id) {
        BaseBomDesc baseBomDesc = baseBomDescService.findById(id).get();
        // QBaseBomDef qbaseBomDef = QBaseBomDef.baseBomDef;

        List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc.getBomId());
        baseBomDesc.setBomDefObjList(allByBomId);
        recursive(allByBomId);
        return ResponseMessage.ok(baseBomDesc);
    }

    public List<BaseBomDesc> recursive(List<BaseBomDef> baseBomDefList) {
        List<BaseBomDesc> baseBomDescslist = new ArrayList<>();
        baseBomDefList.forEach(baseBomDef -> {
            List<BaseBomDesc> allByPartId = baseBomDescService.findAllByPartId(baseBomDef.getPartId());
            baseBomDef.setBomDescObjList(allByPartId);
            allByPartId.forEach(baseBomDesc -> {
                List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc.getBomId());
                baseBomDesc.setBomDefObjList(allByBomId);
                recursive(allByBomId);
            });

        });
        return baseBomDescslist;
    }

    /**
     * 展示BOM
     */
    @GetMapping("/showbomonly")
    @ApiOperation(value = "展示bomonly")
    @UserOperationLog("展示bomonly")
    public ResponseMessage showbomonly(String id) {
        BaseBomDesc baseBomDesc = baseBomDescService.findById(id).get();
        ShowBom showBom = descToShowBomObj(baseBomDesc);


        List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc.getBomId());

        showBom.setShowBomList(recursiveonly(allByBomId));
        return ResponseMessage.ok(showBom);
    }

    public List<ShowBom> recursiveonly(List<BaseBomDef> baseBomDefList) {
        List<ShowBom> showBomDefList = new ArrayList<>();
        baseBomDefList.forEach(baseBomDef -> {
            ShowBom showBomDef = defToShowBomObj(baseBomDef);


            List<BaseBomDesc> allByPartId = baseBomDescService.findAllByPartId(baseBomDef.getPartId());

            List<ShowBom> showBomDescList = new ArrayList<>();

            allByPartId.forEach(baseBomDesc -> {

                ShowBom showBomDesc = descToShowBomObj(baseBomDesc);
                showBomDescList.add(showBomDesc);

                List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc.getBomId());
                showBomDesc.setShowBomList(recursiveonly(allByBomId));
            });

            showBomDef.setShowBomList(showBomDescList);
            showBomDefList.add(showBomDef);
        });
        return showBomDefList;
    }

    public ShowBom descToShowBomObj(BaseBomDesc baseBomDesc) {
        ShowBom showBom = new ShowBom();
        showBom.setPartId(baseBomDesc.getPartId());
        showBom.setDistinguish(baseBomDesc.getDistinguish());
        BaseParts baseParts = basePartsService.selectpartNo(baseBomDesc.getPartId());
        showBom.setName(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getName());
        showBom.setSpec(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSpec());
        showBom.setEffectiveDate(baseBomDesc.getEffectiveDate());
        showBom.setInvalidDate(baseBomDesc.getInvalidDate());
        showBom.setUnit(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSentUnit());
        showBom.setLossRate(baseParts == null ? BigDecimal.ZERO : baseParts.getProductionLossRate());
        showBom.setSource(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSource());

        return showBom;

    }

    public ShowBom defToShowBomObj(BaseBomDef baseBomDef) {
        ShowBom showBom = new ShowBom();
        showBom.setPartId(baseBomDef.getPartId());
        showBom.setDistinguish(baseBomDef.getDistinguish());
        BaseParts baseParts = basePartsService.selectpartNo(baseBomDef.getPartId());
        showBom.setName(baseParts == null ? "空指针错误(物料不存在)" :baseParts.getName());
        showBom.setSpec(baseParts == null ? "空指针错误(物料不存在)" :baseParts.getSpec());
        showBom.setEffectiveDate(baseBomDef.getEffectiveDate());
        showBom.setInvalidDate(baseBomDef.getInvalidDate());
        showBom.setUnit(baseBomDef.getUnit());
        showBom.setLossRate(baseBomDef.getLossRate());
        showBom.setSource(baseParts == null ? "空指针错误(物料不存在)" :baseParts.getSource());

        return showBom;

    }

}