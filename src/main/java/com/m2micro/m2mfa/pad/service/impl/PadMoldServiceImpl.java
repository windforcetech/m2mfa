package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadMoldService;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 14:04
 * @Description: 架模
 */
@Service("padMoldService")
public class PadMoldServiceImpl extends BaseOperateImpl implements PadMoldService {
    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId);
        //架模没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
        return operationInfo;
    }
}
