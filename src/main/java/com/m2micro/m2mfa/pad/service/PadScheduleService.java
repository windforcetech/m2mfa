package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.InitData;
import com.m2micro.m2mfa.pad.model.PadScheduleModel;
import com.m2micro.m2mfa.pad.model.PadStationModel;
import com.m2micro.m2mfa.pad.model.StationAndOperate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 15:03
 * @Description: 排产单
 */
public interface PadScheduleService {

    /**
     * 获取当前用户下的排产单
     * @param icCard
     *          ic卡号
     * @return
     */
    List<PadScheduleModel> getMesMoScheduleByIcCard(String icCard);
    /**
     * 获取当前用户下的排产单
     * @return
     */
    List<PadScheduleModel> getMesMoSchedule();

    /**
     * 获取待处理的工位
     * @param scheduleId
     *          排产单id
     * @return
     */
    List<PadStationModel> getPendingStations(String scheduleId);

    /**
     * 获取待处理的工位及第一个工位操作信息
     * @param scheduleId
     *          排产单id
     * @return
     */
    StationAndOperate getStationsAndOperate(String scheduleId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
