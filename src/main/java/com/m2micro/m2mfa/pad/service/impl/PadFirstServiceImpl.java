package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadFirstService;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 14:03
 * @Description: 首件
 */
@Service("padFirstService")
public class PadFirstServiceImpl extends BaseOperateImpl implements PadFirstService {
    @Override
    public StopWorkModel stopWork(StopWorkPara obj) {
        return super.stopWorkForRecordFail(obj);
    }
}
