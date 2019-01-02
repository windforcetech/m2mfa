package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.InitData;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 15:03
 * @Description: 排产单
 */
public interface PadScheduleService {
    /**
     * 获取当前用户下的初始数据
     * @param staffId
     *          员工工号id
     * @return
     */
    InitData getInitData(String staffId);

    /**
     * 获取当前用户下的排产单
     * @param staffId
     *          员工工号id
     * @return
     */
    MesMoSchedule getMesMoScheduleByStaffId(String staffId);

    /**
     * 获取待处理的工位
     * @param staffId
     *          员工工号id
     * @param scheduleId
     *          排产单id
     * @return
     */
    List<BaseStation> getPendingStations(String staffId, String scheduleId);

    /**
     * 获取操作栏相关信息
     * @param staffId
     * @param scheduleId
     * @param stationId
     */
    OperationInfo getOperationInfo(String staffId, String scheduleId, String stationId);
}
