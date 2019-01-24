package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStaff;
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
        //saveRecordFail(obj);
        MesRecordWork mesRecordWork = findMesRecordWorkById(obj.getRwid());
        MesMoSchedule mesMoSchedule = findMesMoScheduleById(mesRecordWork.getScheduleId());
        IotMachineOutput iotMachineOutput = findIotMachineOutputByMachineId(mesMoSchedule.getMachineId());
        //下工
        stopWorkForOutput(obj.getRwid(),PadStaffUtil.getStaff().getStaffId(),iotMachineOutput);
        //是否已完成目标量
        if(isCompleted(iotMachineOutput,mesMoSchedule,mesRecordWork)){
            //已完成目标量
            return stopWorkForCompleted(obj,stopWorkModel,iotMachineOutput,mesRecordWork, mesMoSchedule);
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

    private StopWorkModel stopWorkForCompleted(StopWorkPara obj,StopWorkModel stopWorkModel,IotMachineOutput iotMachineOutput,MesRecordWork mesRecordWork,MesMoSchedule mesMoSchedule){
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
        BaseStaff baseStaffById = findBaseStaffById(nextMesRecordStaff.getStaffId());
        if(firstMesMoSchedule==null){
            //接班人员做下工处理
            stopWorkForOutput(nextMesRecordStaff.getRwId(),nextMesRecordStaff.getStaffId(),iotMachineOutput);
            String msg = "当前排产单已结束，机台未安排新单，请重新安排接班人"+baseStaffById.getStaffName()+"的工作。 ";
            stopWorkModel.setMsg(msg);
            return stopWorkModel;
        }
        //新排单料号是否与结束相同
        if(!firstMesMoSchedule.getPartId().equals(mesMoSchedule.getPartId())){
            return stopWorkModel;
        }
        //如果相同，处理新排产单
        handleNewSchedule(iotMachineOutput, mesRecordWork, nextMesRecordStaff, firstMesMoSchedule, baseStaffById);
        return stopWorkModel;
    }

    private void handleNewSchedule(IotMachineOutput iotMachineOutput, MesRecordWork mesRecordWork, MesRecordStaff nextMesRecordStaff, MesMoSchedule firstMesMoSchedule, BaseStaff baseStaffById) {
        //添加余料到新排产单

        //删除只上工下工结束时间为空的人员记录(旧排产单记录)
        deleteMesRecordStaffAtlast(nextMesRecordStaff.getRwId());
        //获取旧排产单上工纪录，赋值跟新排产单的纪录进行添加:模具信息
        generateMesRecordWorkandMesRecordMold(mesRecordWork.getScheduleId(),firstMesMoSchedule.getScheduleId(),mesRecordWork.getStationId(),iotMachineOutput);

        //不相同调用接班人员开机上工
        PadPara startPara = new PadPara();
        //新排产单
        startPara.setScheduleId(firstMesMoSchedule.getScheduleId());
        //当前工序
        startPara.setProcessId(mesRecordWork.getProcessId());
        //当前工位
        startPara.setStationId(mesRecordWork.getStationId());
        startWorkForOutputByBaseStaff(startPara,baseStaffById);
    }
}
