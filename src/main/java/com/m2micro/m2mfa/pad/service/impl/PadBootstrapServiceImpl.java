package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadBootstrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 14:02
 * @Description: 开机
 */
@Service("padBootstrapService")
public class PadBootstrapServiceImpl extends BaseOperateImpl implements PadBootstrapService {

    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId);
        //开机没有作业输入，置灰
        operationInfo.setJobInput("0");
        return operationInfo;
    }

    @Override
    public StartWorkPara startWork(PadPara obj) {
        return super.startWorkForOutput(obj);
    }

    @Override
    public StopWorkModel stopWork(StopWorkPara obj) {
        //保存不良输入
        saveRecordFail(obj);
        //下工
        if(isMesRecorWorkEnd(obj.getRwid())){
            //更新上工记录表结束时间
            updateRecordWorkEndTime(obj.getRwid());
        }
        //更新职员作业记录表结束时间
        updateRecordStaffEndTime(obj.getRecordStaffId());

        return super.stopWorkForRecordFail(obj);
    }
}
