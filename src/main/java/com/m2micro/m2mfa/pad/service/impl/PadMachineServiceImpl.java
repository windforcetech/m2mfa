package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStaffService;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadMachineService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.service.MesRecordWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 14:03
 * @Description: 调机
 */
@Service("padMachineService")
public class PadMachineServiceImpl extends BaseOperateImpl implements PadMachineService {
    @Autowired
    private MesRecordWorkService mesRecordWorkService;

    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId) {
        OperationInfo operationInfo = super.getOperationInfo(scheduleId, stationId);
        //调机没有不良输入，置灰
        operationInfo.setDefectiveProducts("0");
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


//    @Override
//    public StartWorkPara startWork(PadPara obj) {
//
//        if(isProcessfirstStation(obj.getProcessId(),obj.getStationId())){
//            updateProcessStarTime(obj.getScheduleId(),obj.getProcessId());
//        }
//
//        if(isStationisWork(obj.getScheduleId(),obj.getStationId())){
//            updateStaffOperationTime(obj.getScheduleId(), PadStaffUtil.getStaff().getStaffId(),obj.getStationId());
//        }else {
//            String rwId =UUIDUtil.getUUID();
//            MesRecordWork mesRecordWork = new MesRecordWork();
//            mesRecordWork.setRwid(rwId);
//            mesRecordWork.setScheduleId(obj.getScheduleId());
//            mesRecordWork.setPartNo();
//            mesRecordWorkService.save(mesRecordWork);
//        }
//        return super.startWork(obj);
//    }


}
