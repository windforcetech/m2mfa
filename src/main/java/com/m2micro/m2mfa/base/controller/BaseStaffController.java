package com.m2micro.m2mfa.base.controller;

import com.m2micro.framework.authorization.Authorize;
import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.starter.entity.User;
import com.m2micro.framework.starter.services.IUserService;
import com.m2micro.framework.starter.services.OrganizationService;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.query.BaseStaffQuery;
import com.m2micro.m2mfa.base.service.BaseStaffService;
import com.m2micro.m2mfa.base.vo.BaseStaffDetailObj;
import com.m2micro.m2mfa.base.vo.BaseStaffQueryObj;
import com.m2micro.m2mfa.base.vo.MesMoscheduleQueryObj;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 员工（职员）表 前端控制器
 *
 * @author liaotao
 * @since 2018-11-26
 */
@RestController
@RequestMapping(value = "/base/baseStaff")
@Api(description = "职工api接口")
@Authorize
public class BaseStaffController {
    @Autowired
    BaseStaffService baseStaffService;
    @Autowired
    IUserService iUserService;

    @Autowired
    OrganizationService organizationService;
    private String defaultPassword = "123456";

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "员工（职员）表列表")
    @UserOperationLog("员工（职员）表列表")
    public ResponseMessage<PageUtil<BaseStaffDetailObj>> list(@RequestBody BaseStaffQuery query) {
        if (StringUtils.isNotEmpty(query.getDepartmentId())) {
            List<String> departmentIds = baseStaffService.getAllIDsOfDepartmentTree(query.getDepartmentId());
            //   List<String> obtainpost = organizationService.obtainpost(baseStaffQueryObj.getDepartmentId());
            query.setDepartmentIds(departmentIds);
        }
        PageUtil<BaseStaffDetailObj> page = baseStaffService.list(query);
        return ResponseMessage.ok(page);
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "员工（职员）表详情")
    @UserOperationLog("员工（职员）表详情")
    public ResponseMessage<BaseStaff> info(@PathVariable("id") String id) {
        BaseStaff baseStaff = baseStaffService.findById(id).orElse(null);
        return ResponseMessage.ok(baseStaff);
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "添加员工（职员）表")
    @UserOperationLog("添加员工（职员）表")
    public ResponseMessage<BaseStaff> save(@RequestBody BaseStaff baseStaff) {
        baseStaff.setGroupId(TokenInfo.getUserGroupId());
        ValidatorUtil.validateEntity(baseStaff, AddGroup.class);
        baseStaff.setStaffId(UUIDUtil.getUUID());
        baseStaff.setDeletionStateCode(false);
        List<BaseStaff> byCodeAndStaffIdNot = baseStaffService.findByCodeAndStaffIdNot(baseStaff.getCode(), "");
        if (byCodeAndStaffIdNot != null && byCodeAndStaffIdNot.size() > 0) {
            throw new MMException("工号不唯一！");
        }
        if(baseStaff.getIcCard()!=null&&baseStaffService.existByIcCard(baseStaff.getIcCard())){
            throw new MMException("卡号不唯一！");
        }
        Boolean needAddUser = baseStaff.getNeedAddUser();
        if (needAddUser) {
            //   T t = ;
            User user = new User();
            user.setLoginName(baseStaff.getCode());
            user.setAdmin(false);
            user.setUserName(baseStaff.getStaffName());
            user.setModifyPassword(defaultPassword);
            //user.setPassword("123");
            user.setInheritMode(true);
            user.setState(1);
            user.setShowIndex(9999);
            List<String> orgIds = new ArrayList<>();
            orgIds.add(baseStaff.getDepartmentId());
            user.setOrgIds(orgIds);
            user.setId(UUIDUtil.getUUID());

            //     User user1 = iUserService.findByLoginNameOrTelOrEmail(user.getLoginName()).get();
            Optional<User> user2 = iUserService.findByLoginNameOrTelOrEmail(user.getLoginName());
            String userId = "";
            if (user2.isPresent()) {
                User mm=user2.get();
                mm.setModifyPassword(defaultPassword);
                mm.setUserName(user.getUserName());
                mm.setOrgIds(orgIds);
                iUserService.updateUser(mm);
                userId = mm.getId();
            } else {
                ResponseMessage responseMessage = iUserService.saveUser(user, TokenInfo.getUserGroupId());
                userId = user.getId();
                if (responseMessage.getStatus() != 200) {
                    return responseMessage;
                }
            }

            baseStaff.setUserId(userId);

        }
        return ResponseMessage.ok(baseStaffService.save(baseStaff));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新员工（职员）表")
    @UserOperationLog("更新员工（职员）表")
    public ResponseMessage<BaseStaff> update(@RequestBody BaseStaff baseStaff) {
        baseStaff.setGroupId(TokenInfo.getUserGroupId());
        ValidatorUtil.validateEntity(baseStaff, UpdateGroup.class);
        BaseStaff baseStaffOld = baseStaffService.findById(baseStaff.getStaffId()).orElse(null);
        if (baseStaffOld == null) {
            throw new MMException("数据库不存在该记录");
        }
        List<BaseStaff> byCodeAndStaffIdNot = baseStaffService.findByCodeAndStaffIdNot(baseStaff.getCode(), baseStaff.getStaffId());
        if (byCodeAndStaffIdNot != null && byCodeAndStaffIdNot.size() > 0) {
            throw new MMException("工号不唯一！");
        }
        if(baseStaff.getIcCard()!=null&&baseStaffService.existByIcCardAndIdNot(baseStaff.getIcCard(),baseStaff.getStaffId())){
            throw new MMException("卡号冲突！");
        }
        Boolean needAddUser = baseStaff.getNeedAddUser();
        if (needAddUser) {
            User user = iUserService.findById(baseStaffOld.getUserId()).get();
            user.setLoginName(baseStaff.getCode());
            user.setAdmin(false);
            user.setUserName(baseStaff.getStaffName());
            user.setModifyPassword(defaultPassword);
            user.setInheritMode(true);
            user.setState(1);
            user.setShowIndex(9999);
            List<String> orgIds = new ArrayList<>();
            orgIds.add(baseStaff.getDepartmentId());
            user.setOrgIds(orgIds);
            ResponseMessage responseMessage = iUserService.updateUser(user);
            if (responseMessage.getStatus() != 200) {
                return responseMessage;
            }
        }
        PropertyUtil.copy(baseStaff, baseStaffOld);
        return ResponseMessage.ok(baseStaffService.save(baseStaffOld));
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除员工（职员）表")
    @UserOperationLog("删除员工（职员）表")
    public ResponseMessage delete(@RequestBody String[] ids) {
        //baseStaffService.deleteByIds(ids);
//        if (baseStaffService.isUsedForStaff(ids)) {
//            throw new MMException("职员被使用不可删除！");
//        }

//        for (String id :ids){
//            BaseStaff baseStaff = baseStaffService.findById(id).get();
//            baseStaff.setDeletionStateCode(true);
//            baseStaffService.save(baseStaff);
//        }

        return   baseStaffService.deleteByStaffId(ids);
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/productionlist", method = RequestMethod.POST)
    @ApiOperation(value = "排产单员工（职员）表列表")
    @UserOperationLog("排产单员工（职员）表列表")
    public ResponseMessage<List<BaseStaff>> productionlist(@RequestBody MesMoscheduleQueryObj baseStaffQueryObj) {

        List<BaseStaff> page = baseStaffService.productionlist(baseStaffQueryObj);

        return ResponseMessage.ok(page);
    }

}
