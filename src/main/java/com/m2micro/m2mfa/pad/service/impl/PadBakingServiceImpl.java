package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadBakingService;
import org.springframework.stereotype.Service;

/**
 * @Auther: chenshuhong
 * @Date: 2019/1/19 10:20
 * @Description: 烤料
 */
@Service("padBakingService")
public class PadBakingServiceImpl extends BaseOperateImpl  implements PadBakingService {


    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId);
        //烤料没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
        //烤料没有作业输入，置灰
        operationInfo.setJobInput("0");
        return operationInfo;
    }
}
