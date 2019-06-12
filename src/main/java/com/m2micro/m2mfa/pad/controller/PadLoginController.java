package com.m2micro.m2mfa.pad.controller;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.pad.model.LoginData;
import com.m2micro.m2mfa.pad.model.PadScheduleModel;
import com.m2micro.m2mfa.pad.service.PadScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/16 11:44
 * @Description:
 */

@RestController
@RequestMapping("pad/padLogin")
@Api(description = "员工登录相关接口")
public class PadLoginController {
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    PadScheduleService padScheduleService;

    @RequestMapping("/login")
    @ApiOperation(value="pad登录")
    @UserOperationLog("pad登录")
    public ResponseMessage<LoginData> login(String icCard){
        if(StringUtils.isEmpty(icCard)){
            return ResponseMessage.error("用员工卡号不能为空");
        }
        //当前员工不存在
        BaseStaff baseStaff = baseStaffRepository.findByIcCard(icCard);
        if(baseStaff==null){
            return ResponseMessage.error("当前员工不存在");
        }
        //创建token
        TokenInfo token = new TokenInfo();
        token.setTokenID(UUIDUtil.getUUID());
        token.setUserID(baseStaff.getStaffId());
        token.setGroupID(baseStaff.getGroupId());
        TokenInfo.SetToken(token);
        //获取员工下的排产单
        List<PadScheduleModel> mesMoSchedule = padScheduleService.getMesMoScheduleByIcCard(icCard);
        if(mesMoSchedule==null||mesMoSchedule.size()==0){
            return ResponseMessage.error("当前员工没有分派任务！");
        }
        //设置返回数据
        LoginData loginData = new LoginData();
        loginData.setTokenId(token.getTokenID());
        loginData.setSchedules(mesMoSchedule);
        return ResponseMessage.ok(loginData);
    }
}
