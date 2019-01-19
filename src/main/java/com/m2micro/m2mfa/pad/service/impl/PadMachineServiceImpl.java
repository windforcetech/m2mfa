package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadMachineService;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 14:03
 * @Description: 调机
 */
@Service("padMachineService")
public class PadMachineServiceImpl extends BaseOperateImpl implements PadMachineService {
    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId);
        //调机没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
        return operationInfo;
    }
}
