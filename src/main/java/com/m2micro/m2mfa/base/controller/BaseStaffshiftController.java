package com.m2micro.m2mfa.base.controller;

import com.google.common.collect.Lists;
import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;
import com.m2micro.m2mfa.base.service.BaseShiftService;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import com.m2micro.m2mfa.base.service.BaseStaffshiftService;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.vo.OneStaffShift;
import com.m2micro.m2mfa.base.vo.StaffShiftByOne;
import com.m2micro.m2mfa.base.vo.StaffShiftObj;
import com.m2micro.m2mfa.base.vo.StaffShiftResult;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/base/baseStaffshift")
@Api(description = "员工排班表 前端控制器")
@Authorize
public class BaseStaffshiftController implements BaseController<BaseStaffshift, String, BaseStaffshiftQuery> {
    @Autowired
    BaseStaffshiftService baseStaffshiftService;

    @Autowired
    BaseStaffService baseStaffService;
    @Autowired
    BaseShiftService baseShiftService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "员工排班表列表")
    @UserOperationLog("员工排班表列表")
    public ResponseMessage<PageUtil<Map<String, Object>>> list(BaseStaffshiftQuery query) {
        PageUtil<Map<String, Object>> page = baseStaffshiftService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 列表
     */
    @PostMapping("/listbystaff")
    @ApiOperation(value = "单个员工排班表列表")
    @UserOperationLog("单个员工排班表列表")
    public ResponseMessage<StaffShiftByOne> listbystaff(BaseStaffshiftQuery query) {
        QBaseStaffshift qBaseStaff = QBaseStaffshift.baseStaffshift;
        BooleanExpression expression = qBaseStaff.shiftDate.between(query.getStartTime(), query.getEndTime())
                .and(qBaseStaff.staffId.eq(query.getStaffId())).and(qBaseStaff.groupId.eq(TokenInfo.getUserGroupId()));
        ArrayList<BaseStaffshift> baseStaffs = Lists.newArrayList(baseStaffshiftService.findAll(expression));

//        List<String> baseShiftIdList = baseStaffs.stream()
//                .map(BaseStaffshift::getShiftId).distinct()
//                .collect(Collectors.toList());
//        Map<String, String> baseShiftNameMap = baseShiftService.findAllById(baseShiftIdList)
//                .stream()
//                .collect(Collectors.toMap(BaseShift::getShiftId, BaseShift::getName));
//
//        Map<String, List<BaseStaffshift>> baseStaffshitMap = baseStaffs.stream()
//                .map(item -> {
//
//                })
//                .collect(Collectors.groupingBy(BaseStaffshift::getStaffId));
//
//        Set<String> keys = baseStaffshitMap.keySet();
//
//        List<HashMap<String, Object>> collect = baseStaffService.findAllById(Lists.newArrayList(keys))
//                .stream()
//                .map(item -> {
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("staff_id", item.getStaffId());
//                    map.put("keyvalue", baseStaffshitMap.get(item.getStaffId()));
//                    map.put("code", item.getCode());
//                    return map;
//                }).collect(Collectors.toList());


        Stream<BaseStaffshift> sorted = baseStaffs.stream().sorted(Comparator.comparing(BaseStaffshift::getShiftDate));
        StaffShiftByOne staffShiftByOne = new StaffShiftByOne();
        staffShiftByOne.setStaffId(query.getStaffId());

        Optional<BaseStaff> byId = baseStaffService.findById(query.getStaffId());
        BaseStaff baseStaff = byId.get();
        staffShiftByOne.setStaffName(baseStaff.getStaffName());
        staffShiftByOne.setStaffCode(baseStaff.getCode());

        List<OneStaffShift> list =
                sorted.map(item -> {
                    OneStaffShift oneStaffShift = new OneStaffShift();
                    oneStaffShift.setId(item.getId());
                    oneStaffShift.setShiftDate(item.getShiftDate());
                    oneStaffShift.setShiftId(item.getShiftId());

                    Optional<BaseShift> byId1 = baseShiftService.findById(item.getShiftId());
                    BaseShift baseShift = byId1.get();
                    oneStaffShift.setShiftName(baseShift.getName());
                    oneStaffShift.setShiftCode(baseShift.getCode());

                    return oneStaffShift;
                }).collect(Collectors.toList());

        staffShiftByOne.setList(list);
        return ResponseMessage.ok(staffShiftByOne);
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "员工排班表详情")
    @UserOperationLog("员工排班表详情")
    public ResponseMessage<BaseStaffshift> info(@PathVariable("id") String id) {
        BaseStaffshift baseStaffshift = baseStaffshiftService.findById(id).orElse(null);
        return ResponseMessage.ok(baseStaffshift);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存员工排班表")
    @UserOperationLog("保存员工排班表")
    public ResponseMessage<List<StaffShiftResult>> save(@RequestBody StaffShiftObj staffShiftObj) throws ParseException {
        String groupId = TokenInfo.getUserGroupId();
        ValidatorUtil.validateEntity(staffShiftObj, AddGroup.class);
        List<BaseStaffshift> baseStaffshiftList = new ArrayList<>();
        List<String> stringst;
        //员工id为空，取部门所有人员
        if (StringUtils.isEmpty(staffShiftObj.getStaffId())) {
            QBaseStaff qBaseStaff = QBaseStaff.baseStaff;
            BooleanExpression expression = qBaseStaff.departmentId.eq(staffShiftObj.getDepartmentId()).and(qBaseStaff.groupId.eq(groupId));
            ArrayList<BaseStaff> baseStaffs = Lists.newArrayList(baseStaffService.findAll(expression));
            stringst = baseStaffs.stream().map(x -> x.getStaffId()).collect(Collectors.toList());
        } else {
            stringst = Arrays.asList(staffShiftObj.getStaffId().split(","));
        }
        //排除人员不为空,将他们删除
        if (StringUtils.isNotEmpty(staffShiftObj.getExcludeStaffId())) {
            String[] split = staffShiftObj.getExcludeStaffId().split(",");
            List<String> strings = Arrays.asList(split);
            //stringst.removeAll(strings);
            stringst = stringst.stream()
                    .filter(id -> !strings.contains(id))
                    .collect(Collectors.toList());
        }
        if (stringst.size() == 0) {
            throw new MMException("员工个数为零");
        }
        //遍历所有人员
        for (String baseStaff :
                stringst) {
            Calendar c = Calendar.getInstance();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = dateFormat1.parse(dateFormat1.format(staffShiftObj.getStartDate()));
            Date endDate = dateFormat1.parse(dateFormat1.format(staffShiftObj.getEndDate()));
            c.setTime(endDate);
            c.add(Calendar.DATE, 1); // 结束日期加1天
            endDate = c.getTime();
            Date date = beginDate;
            List<String> strings1 = new ArrayList<>();//要排除日期
            if (StringUtils.isNotEmpty(staffShiftObj.getExcludeDate())) {
                String[] split1 = staffShiftObj.getExcludeDate().split(",");
                strings1 = Arrays.asList(split1);
            }
            //遍历所有时间
            while (!date.equals(endDate)) {
                //不是要排除的日期，添加
                if (!strings1.contains(dateFormat1.format(date))) {
                    BaseStaffshift baseStaffshift = new BaseStaffshift();
                    baseStaffshift.setId(UUIDUtil.getUUID());
                    baseStaffshift.setEnabled(true);
                    baseStaffshift.setShiftId(staffShiftObj.getShiftId());
                    baseStaffshift.setStaffId(baseStaff);
                    baseStaffshift.setShiftDate(date);
                    baseStaffshiftList.add(baseStaffshift);

                }
                System.out.println(date);
                c.setTime(date);
                c.add(Calendar.DATE, 1); // 日期加1天
                date = c.getTime();
            }
        }

        ArrayList<StaffShiftResult> staffShiftResults = new ArrayList<>();

        for (BaseStaffshift baseStaffshift :
                baseStaffshiftList) {
            StaffShiftResult staffShiftResult = new StaffShiftResult();
            staffShiftResult.setShiftDate(baseStaffshift.getShiftDate());

            Optional<BaseStaff> byId = baseStaffService.findById(baseStaffshift.getStaffId());
            BaseStaff baseStaff = byId.get();
            staffShiftResult.setStaffName(baseStaff.getStaffName());
            staffShiftResult.setStaffCode(baseStaff.getCode());

            Optional<BaseShift> byId1 = baseShiftService.findById(baseStaffshift.getShiftId());
            BaseShift baseShift = byId1.get();
            staffShiftResult.setShiftName(baseShift.getName());
            staffShiftResult.setShiftCode(baseShift.getCode());

            if (staffShiftObj.getForceUpdate()) {
                //强制更新，先删除原来所有，再新增一条
                QBaseStaffshift qBaseStaff = QBaseStaffshift.baseStaffshift;
                BooleanExpression expression = qBaseStaff.shiftDate.eq(baseStaffshift.getShiftDate())
                        //.and(qBaseStaff.shiftId.eq(baseStaffshift.getShiftId()))
                        .and(qBaseStaff.staffId.eq(baseStaffshift.getStaffId()))
                        .and(qBaseStaff.groupId.eq(groupId));
                ArrayList<BaseStaffshift> baseStaffs = Lists.newArrayList(baseStaffshiftService.findAll(expression));
                if (baseStaffs.size() > 0) {

                    //先删除所有
                    baseStaffshiftService.deleteAll(baseStaffs);

                    /*BaseStaffshift baseStaffshift1 = baseStaffs.get(0);
                    baseStaffshift1.setShiftId(baseStaffshift.getShiftId());
                    baseBomDescService.updateById(baseStaffshift1.getId(), baseStaffshift1);*/

                    //再新增一条
                    baseStaffshiftService.save(baseStaffshift);
                    staffShiftResult.setResult("更新成功");

                } else {
                    baseStaffshiftService.save(baseStaffshift);
                    staffShiftResult.setResult("新增成功");

                }
            } else {
                QBaseStaffshift qBaseStaff = QBaseStaffshift.baseStaffshift;
                BooleanExpression expression = qBaseStaff.shiftDate.eq(baseStaffshift.getShiftDate())
                        .and(qBaseStaff.shiftId.eq(baseStaffshift.getShiftId()))
                        .and(qBaseStaff.staffId.eq(baseStaffshift.getStaffId()))
                        .and(qBaseStaff.groupId.eq(groupId));
                ArrayList<BaseStaffshift> baseStaffs = Lists.newArrayList(baseStaffshiftService.findAll(expression));
                if (baseStaffs.size() == 0) {
                    baseStaffshiftService.save(baseStaffshift);
                    staffShiftResult.setResult("新增成功");
                } else {
                    staffShiftResult.setResult("已存在排班，没有选择强制替换");
                }

            }
            staffShiftResults.add(staffShiftResult);
        }

        return ResponseMessage.ok(staffShiftResults);
    }

    /**
     * 更新
     */
    @PostMapping("/updateall")
    @ApiOperation(value = "更新所有员工排班表")
    @UserOperationLog("更新所有员工排班表")
    public ResponseMessage<List<BaseStaffshift>> updateall(@RequestBody List<BaseStaffshift> baseStaffshift) {

        List<BaseStaffshift> list = new ArrayList<>();
        List<String> listDelete = new ArrayList<>();
        for (BaseStaffshift baseStaffshift1 : baseStaffshift) {
            ValidatorUtil.validateEntity(baseStaffshift1, UpdateGroup.class);
            BaseStaffshift baseStaffshiftOld = baseStaffshiftService.findById(baseStaffshift1.getId()).orElse(null);
            if (baseStaffshiftOld == null) {
                throw new MMException("数据库不存在该记录");
            }
            if (StringUtils.isEmpty(baseStaffshift1.getShiftId())) {//为null,删除
                listDelete.add(baseStaffshift1.getId());

            } else {
                PropertyUtil.copy(baseStaffshift1, baseStaffshiftOld);
                list.add(baseStaffshiftOld);
            }
        }
        baseStaffshiftService.deleteByIds(listDelete.toArray(new String[0]));
        return ResponseMessage.ok(baseStaffshiftService.saveAll(list));
    }


    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新员工排班表")
    @UserOperationLog("更新员工排班表")
    public ResponseMessage<BaseStaffshift> update(@RequestBody BaseStaffshift baseStaffshift) {
        ValidatorUtil.validateEntity(baseStaffshift, UpdateGroup.class);
        BaseStaffshift baseStaffshiftOld = baseStaffshiftService.findById(baseStaffshift.getId()).orElse(null);
        if (baseStaffshiftOld == null) {
            throw new MMException("数据库不存在该记录");
        }
        PropertyUtil.copy(baseStaffshift, baseStaffshiftOld);
        return ResponseMessage.ok(baseStaffshiftService.save(baseStaffshiftOld));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除员工排班表")
    @UserOperationLog("删除员工排班表")
    public ResponseMessage delete(@RequestBody String[] ids) {

        String[] canDelete = baseStaffshiftService.findCanDelete(ids);
        baseStaffshiftService.deleteByIds(substract(ids, canDelete));
        if (canDelete.length > 0) {
            throw new MMException(StringUtils.join(canDelete, ",") + "存在排产记录，不可删除");
        }

        return ResponseMessage.ok();
    }

    public static String[] substract(String[] arr1, String[] arr2) {
        LinkedList<String> list = new LinkedList<String>();
        for (String str : arr1) {
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) {
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        String[] result = {};
        return list.toArray(result);
    }

    @Override
    public BaseService<BaseStaffshift, String> getService() {
        return baseStaffshiftService;
    }
}