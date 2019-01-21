package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadCommonService;
import org.springframework.stereotype.Service;

@Service("padCommonService")
public class PadCommonServiceImpl extends BaseOperateImpl implements PadCommonService {

    @Override
    public StartWorkPara startWork(PadPara obj) {
        return super.startWork(obj);
    }

    @Override
    public StopWorkModel stopWork(StopWorkPara obj) {
        return super.stopWorkForRecordFail(obj);
    }
}
