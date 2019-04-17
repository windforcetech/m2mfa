package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.*;
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
    public OperationInfo getOperationInfo(String scheduleId, String stationId,String processId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId,processId);
        //烤料没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
        //烤料没有作业输入，置灰
        operationInfo.setJobInput("0");
        return operationInfo;
    }

    @Override
    public StartWorkPara startWork(PadPara obj) {
        return super.startWorkForOutput(obj);
    }

    @Override
    public StopWorkModel stopWork(StopWorkPara obj) {
        return super.stopWork(obj);
    }

    @Override
    public Object defectiveProducts(Padbad padbad) {

        return super.defectiveProducts(padbad);
    }
}
