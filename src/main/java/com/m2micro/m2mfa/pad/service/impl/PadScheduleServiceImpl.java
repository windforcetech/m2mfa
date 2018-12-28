package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.InitData;
import com.m2micro.m2mfa.pad.service.PadScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 15:03
 * @Description:
 */
@Service
public class PadScheduleServiceImpl implements PadScheduleService {
    @Autowired
    MesMoScheduleService mesMoScheduleService;

    @Override
    public InitData getInitData(String staffId) {
        InitData initData = new InitData();
        MesMoSchedule mesMoSchedule = mesMoScheduleService.getMesMoScheduleByStaffId(staffId);
        if(mesMoSchedule!=null){
            initData.setMesMoSchedule(mesMoSchedule);
            initData.setBaseStations(mesMoScheduleService.getPendingStations(staffId, mesMoSchedule.getScheduleId()));
        }
        return initData;
    }

    @Override
    public MesMoSchedule getMesMoScheduleByStaffId(String staffId) {
        return mesMoScheduleService.getMesMoScheduleByStaffId(staffId);
    }

    @Override
    public List<BaseStation> getPendingStations(String staffId, String scheduleId) {
        return mesMoScheduleService.getPendingStations(staffId, scheduleId);
    }

    @Override
    public OperationInfo getOperationInfo(String staffId, String scheduleId, String stationId) {
        return mesMoScheduleService.getOperationInfo(staffId, scheduleId, stationId);
    }
}
