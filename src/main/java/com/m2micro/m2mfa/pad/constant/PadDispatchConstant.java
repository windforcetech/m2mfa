package com.m2micro.m2mfa.pad.constant;

import com.m2micro.m2mfa.pad.service.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @Auther: liaotao
 * @Date: 2019/1/14 17:19
 * @Description:
 */
public class PadDispatchConstant {

    private static HashMap handle = new HashMap();

    static {
        handle.put("10000022", PadMachineService.class.getName());//调机
        handle.put("10000021", PadMoldService.class.getName());//架模
        handle.put("10000020", PadChargingService.class.getName());//加料
        handle.put("10000039", PadBootstrapService.class.getName());//开机

        /*
        handle.put("10000023", PadFirstService.class.getName());//首件
        handle.put("10000053", PadFinalInspectionService.class.getName());//终检
        handle.put("10000044", PadFrontierInspectionService.class.getName());//边检*/

    }

    public static String getHandle(String key){
        String handleName = (String)handle.get(key);
        if(StringUtils.isEmpty(handleName)){
            return PadFrontierInspectionService.class.getName();
        }
        return handleName;
    }
}
