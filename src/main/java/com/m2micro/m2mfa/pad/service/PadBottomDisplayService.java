package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.pad.model.MoDescInfoModel;

/**
 * @Auther: liaotao
 * @Date: 2019/2/19 13:59
 * @Description:
 */
public interface PadBottomDisplayService {

    MoDescInfoModel getMoDescInfo(String scheduleId);
}
