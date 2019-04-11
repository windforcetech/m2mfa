package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadChargingService;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 14:43
 * @Description: 加料
 */
@Service("padChargingService")
public class PadChargingServiceImpl extends BaseOperateImpl implements PadChargingService {
    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId,String processId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId,processId);
        //加料没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
        return operationInfo;
    }

    @Override
    public StartWorkPara startWork(PadPara obj) {
        return super.startWorkForOutput(obj);
    }

    @Override
    public StopWorkModel stopWork(StopWorkPara obj) {
        return super.stopWorkForRecordFail(obj);
    }
}
