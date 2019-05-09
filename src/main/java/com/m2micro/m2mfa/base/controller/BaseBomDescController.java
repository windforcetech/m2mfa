package com.m2micro.m2mfa.base.controller;

import com.google.common.collect.Lists;
import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseBomDescQuery;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.base.vo.ShowBom;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

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
    @Autowired
    BaseItemsTargetService baseItemsTargetService;
    @Autowired
    JPAQueryFactory queryFactory;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "bom列表")
    @UserOperationLog("bom列表")
    public ResponseMessage<PageUtil<BaseBomDesc>> list(BaseBomDescQuery query) {
        QBaseBomDesc baseBomDesc = QBaseBomDesc.baseBomDesc;
        JPAQuery<BaseBomDesc> jq = queryFactory.selectFrom(baseBomDesc);
        BooleanBuilder expression = new BooleanBuilder();
        String[] partIds = new String[0];
        if (StringUtils.isNotEmpty(query.getPartsCategory())) {
            BaseItemsTarget baseItemsTarget = baseItemsTargetService.findById(query.getPartsCategory()).orElse(null);
            //不等于全部
            if (!(baseItemsTarget != null && "全部".equals(baseItemsTarget.getItemName()))) {
                List<BaseParts> allByCategory = basePartsService.findAllByCategory(query.getPartsCategory());
                partIds = allByCategory.stream().map(x -> x.getPartNo()).collect(Collectors.toList()).toArray(new String[0]);
                expression.and((baseBomDesc.partId.in(partIds)));
            }
        }
        if (StringUtils.isNotEmpty(query.getPartId())) {
            expression.and(baseBomDesc.partId.like("%" + query.getPartId() + "%"));
        }
        if (StringUtils.isNotEmpty(query.getDistinguish())) {
            expression.and((baseBomDesc.distinguish.like("%" + query.getDistinguish() + "%")));
        }
        if (query.getVersion()!=null) {
            expression.and((baseBomDesc.version.eq(query.getVersion())));
        }
        if (StringUtils.isNotEmpty(query.getCategory())) {
            expression.and((baseBomDesc.category.eq(query.getCategory())));
        }
        if (query.getCheckFlag()!=null) {
            if(query.getCheckFlag()){
                expression.and((baseBomDesc.checkFlag.isNull()));
            }else{
                expression.and((baseBomDesc.checkFlag.eq(true)));
            }
        }
        if (query.getEnabled()!=null) {
            expression.and((baseBomDesc.enabled.eq(query.getEnabled())));
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            expression.and((baseBomDesc.description.like("%" + query.getDescription() + "%")));
        }
        OrderSpecifier orderSpecifier = null;
        if(orderSpecifier==null||"createOn".equals(query.getDirect())){
            orderSpecifier = baseBomDesc.createOn.desc();
        }
        orderSpecifier = addOrderCondition(orderSpecifier,query,baseBomDesc);

        jq.where(expression).orderBy(orderSpecifier).orderBy(baseBomDesc.createOn.desc());
        List<BaseBomDesc> baseBomDescs = jq.fetch();
        //List<BaseBomDesc> baseBomDescs = Lists.newArrayList(baseBomDescService.findAll(expression));
        //查询对应的类型名称
        List<SelectNode> bomCategory = baseItemsTargetService.getSelectNode("Bom_Category");
        baseBomDescs.forEach(baseBomDesc1 -> {
            BaseParts baseParts = basePartsService.findById(baseBomDesc1.getPartId()).orElse(null);
            baseBomDesc1.setName(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getName());
            baseBomDesc1.setSpec(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSpec());
            baseBomDesc1.setProductionUnit(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getProductionUnit());

            List<BaseBomDef> allByBomId = baseBomDefService.findAllByBomId(baseBomDesc1.getBomId());
            allByBomId.forEach(baseBomDef -> {
                BaseParts baseParts1 = basePartsService.findById(baseBomDef.getPartId()).orElse(null);
                baseBomDef.setName(baseParts1 == null ? "空指针错误(物料不存在)" : baseParts1.getName());
            });

            List<BaseBomSubstitute> baseBomSubstituteList = baseBomSubstituteService.findAllByBomId(baseBomDesc1.getBomId());
            baseBomDesc1.setBomSubstituteList(baseBomSubstituteList);

            baseBomDesc1.setBomDefObjList(allByBomId);

            for (SelectNode selectNode : bomCategory) {
                if(selectNode.getId().equals(baseBomDesc1.getCategory())){
                    baseBomDesc1.setCategory(selectNode.getName());
                }
            }

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
    public ResponseMessage<List<BaseBomDef>> save(@RequestBody BaseBomDesc staffShiftObj) throws ParseException, InterruptedException {
        ValidatorUtil.validateEntity(staffShiftObj, AddGroup.class);

        List<BaseBomDef> bomDefObjList = staffShiftObj.getBomDefObjList();


        List<String> addPartIds = new ArrayList<>();
        bomDefObjList.forEach(x -> {
            addPartIds.add(x.getPartId());
        });
        List<String> bomdescPartIds = new ArrayList<>();
        boolean b = checkDeadLoop(staffShiftObj, bomdescPartIds, addPartIds);
        if (b) {
            throw new MMException("物料编号" + bomdescPartIds.get(0) + "形成死环！");
        }

        String s = checkLoop(staffShiftObj);
        if (s != null) {
            throw new MMException("物料编号" + s + "形成死环！");
        }
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

        bomDefObjList.forEach(x -> {
            x.setBomId(save.getBomId());
            x.setId(UUIDUtil.getUUID());
        });

        return ResponseMessage.ok(baseBomDefService.saveAll(bomDefObjList));
    }

    private String checkLoop(BaseBomDesc baseBomDescObj) throws InterruptedException {
        ArrayBlockingQueue<String> searchingIds = new ArrayBlockingQueue<String>(10000);
        List<String> searchedIds = new ArrayList<>();

        searchingIds.add(baseBomDescObj.getPartId());
        while (searchingIds.size() > 0) {
            String partId = searchingIds.take();
            searchedIds.add((partId));

            QBaseBomDef qBaseBomDef = QBaseBomDef.baseBomDef;
            BooleanExpression Expression = qBaseBomDef.partId.eq(partId);
            Iterable<BaseBomDef> bomDefIterable = baseBomDefService.findAll(Expression);

            bomDefIterable.forEach(def -> {
                BaseBomDesc baseBomDesc = baseBomDescService.findById(def.getBomId()).orElse(null);
                searchingIds.add(baseBomDesc.getPartId());
            });
        }

        List<String> addPartIds = new ArrayList<>();
        List<BaseBomDef> bomDefObjList = baseBomDescObj.getBomDefObjList();
        bomDefObjList.forEach(x -> {
            addPartIds.add(x.getPartId());
        });

        for (String addpartId : addPartIds) {
            if (searchedIds.contains(addpartId)) {
                return addpartId;
            }
        }
        return null;

    }

    //    QBaseBomDesc qBaseBomDesc = QBaseBomDesc.baseBomDesc;
//    BooleanExpression booleanExpression = qBaseBomDesc.partId.eq(partId);
//    Iterable<BaseBomDesc> bomDescIterable = baseBomDescService.findAll(booleanExpression);//partId关系，
//
//            bomDefIterable.forEach(desc -> {
//        searchingIds.add(desc.getPartId());
//    });
    private boolean checkDeadLoop(BaseBomDesc staffShiftObj, List<String> partIds, List<String> addPartIds) {
        QBaseBomDef qBaseBomDef = QBaseBomDef.baseBomDef;
        BooleanExpression booleanExpression = qBaseBomDef.partId.eq(staffShiftObj.getPartId());
        //一个主表通过partId与多个子表关联
        Iterable<BaseBomDef> bomDefIterable = baseBomDefService.findAll(booleanExpression);

        for (BaseBomDef baseBomDef : bomDefIterable) {

            //一个子表只有一个父表
            BaseBomDesc baseBomDesc = baseBomDescService.findById(baseBomDef.getBomId()).orElse(null);
            partIds.add(baseBomDesc.getPartId());
            for (String addpartId : addPartIds) {
                if (partIds.contains(addpartId)) {
                    partIds.clear();
                    partIds.add(addpartId);
                    return true;
                }
            }

            return checkDeadLoop(baseBomDesc, partIds, addPartIds);

        }

        return false;
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
        BaseParts baseParts = basePartsService.findById(baseBomDesc.getPartId()).orElse(null);
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
        BaseParts baseParts = basePartsService.findById(baseBomDef.getPartId()).orElse(null);
        showBom.setName(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getName());
        showBom.setSpec(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSpec());
        showBom.setEffectiveDate(baseBomDef.getEffectiveDate());
        showBom.setInvalidDate(baseBomDef.getInvalidDate());
        showBom.setUnit(baseBomDef.getUnit());
        showBom.setLossRate(baseBomDef.getLossRate());
        showBom.setSource(baseParts == null ? "空指针错误(物料不存在)" : baseParts.getSource());

        return showBom;

    }

    private OrderSpecifier addOrderCondition(OrderSpecifier orderSpecifier, BaseBomDescQuery query, QBaseBomDesc baseBomDesc) {
        if(StringUtils.isNotEmpty(query.getDirect())){
            if("partId".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.partId.desc();
                }else{
                    orderSpecifier = baseBomDesc.partId.asc();
                }
            }
            if("category".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.category.desc();
                }else{
                    orderSpecifier = baseBomDesc.category.asc();
                }
            }
            if("enabled".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.enabled.desc();
                }else{
                    orderSpecifier = baseBomDesc.enabled.asc();
                }
            }
            if("effectiveDate".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.effectiveDate.desc();
                }else{
                    orderSpecifier = baseBomDesc.effectiveDate.asc();
                }
            }
            if("invalidDate".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.invalidDate.desc();
                }else{
                    orderSpecifier = baseBomDesc.invalidDate.asc();
                }
            }
            if("checkFlag".equals(query.getOrder())){
                if("desc".equalsIgnoreCase(query.getDirect())){
                    orderSpecifier = baseBomDesc.checkFlag.desc();
                }else{
                    orderSpecifier = baseBomDesc.checkFlag.asc();
                }
            }

        }
        return orderSpecifier;
    }

}
