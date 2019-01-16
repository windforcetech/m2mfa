package com.m2micro.m2mfa.pad.util;

import com.m2micro.framework.authorization.TokenInfo;
import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.SpringContextUtil;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.service.BaseStaffService;

/**
 * @Auther: liaotao
 * @Date: 2019/1/16 14:03
 * @Description:
 */
public class PadStaffUtil {

    /**
     * 获取当前员工信息，没有获取抛异常
     * @return 当前员工信息
     */
    public static BaseStaff getStaff(){
        BaseStaff baseStaff = getStaffOrNull();
        if(baseStaff==null){
            throw new MMException("当前用户不存在","401");
        }
        return baseStaff;
    }

    /**
     * 获取当前员工信息，没有获取为null
     * @return 当前员工信息
     */
    public static BaseStaff getStaffOrNull(){
        TokenInfo token = TokenInfo.getTokenInfo("");
        BaseStaffService baseStaffService = SpringContextUtil.getBean(BaseStaffService.class);
        return baseStaffService.findById(token.getUserID()).orElse(null);
    }
}
