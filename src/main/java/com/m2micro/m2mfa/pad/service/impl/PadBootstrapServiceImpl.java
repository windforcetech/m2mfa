package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;
import com.m2micro.m2mfa.pad.operate.BaseOperateImpl;
import com.m2micro.m2mfa.pad.service.PadBootstrapService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public StopWorkModel stopWork(StopWorkPara obj) {
        //预留，现阶段内没用
        StopWorkModel stopWorkModel = new StopWorkModel();
        //保存不良输入
        saveRecordFail(obj);

        MesRecordWork mesRecordWork = findMesRecordWorkById(obj.getRwid());
        MesMoSchedule mesMoSchedule = findMesMoScheduleById(mesRecordWork.getScheduleId());
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        //下工
        if(isMesRecorWorkEnd(obj.getRwid())){
            //更新上工记录表结束时间
            updateMesRecordWorkEndTime(iotMachineOutput,obj.getRwid());
        }
        //更新职员作业记录表结束时间(dai-------------)
        //

        //是否已完成目标量
        if(isCompleted(iotMachineOutput,mesMoSchedule)){
            //已完成目标量
            return stopWorkForCompleted(obj,stopWorkModel,iotMachineOutput,mesRecordWork);
        }
        //没有完成目标量
        return stopWorkForUnCompleted(obj,stopWorkModel,iotMachineOutput,mesRecordWork);
    }

    private StopWorkModel stopWorkForUnCompleted(StopWorkPara obj,StopWorkModel stopWorkModel,IotMachineOutput iotMachineOutput,MesRecordWork mesRecordWork ){
        //是否交接班
        if(!isChangeShifts(PadStaffUtil.getStaff().getStaffId())){
            //不交接班
            return stopWorkModel;
        }
        //获取接班人员上工记录
        MesRecordStaff nextMesRecordStaff = getNextMesRecordStaff(mesRecordWork.getScheduleId(), obj.getStationId());
        if(nextMesRecordStaff==null){
            return stopWorkModel;
        }
        //更新接班人员 开始产量、开始电量
        MesRecordStaff mesRecordStaff = findMesRecordStaffById(obj.getRecordStaffId());
        updateNextMesRecordStaff(iotMachineOutput,mesRecordStaff);
        return stopWorkModel;
    }

    private StopWorkModel stopWorkForCompleted(StopWorkPara obj,StopWorkModel stopWorkModel,IotMachineOutput iotMachineOutput,MesRecordWork mesRecordWork){
        //退料
        //下模
        //结束工序
        endProcessEndTime(mesRecordWork.getScheduleId(),mesRecordWork.getProcessId());
        //是否交接班
        if(!isChangeShifts(PadStaffUtil.getStaff().getStaffId())){
            //不交接班
            return stopWorkModel;
        }
        //获取接班人员上工记录
        MesRecordStaff nextMesRecordStaff = getNextMesRecordStaff(mesRecordWork.getScheduleId(), obj.getStationId());
        if(nextMesRecordStaff==null){
            return stopWorkModel;
        }

        //获取新的排产单
        MesMoSchedule firstMesMoSchedule = getFirstMesMoScheduleByMachineId(mesRecordWork.getMachineId());
        if(firstMesMoSchedule==null){

        }
        return stopWorkModel;
    }
}
