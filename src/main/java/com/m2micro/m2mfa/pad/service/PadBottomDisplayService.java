package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.pad.model.MoDescInfoModel;
import com.m2micro.m2mfa.pad.model.StationInfoModel;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/2/19 13:59
 * @Description:
 */
public interface PadBottomDisplayService {

    /**
     * 获取工单相关信息
     * @param scheduleId
     *          排产单id
     * @return
     */
    MoDescInfoModel getMoDescInfo(String scheduleId);

    /**
     * 获取工位作业信息及进度
     * @param scheduleId
     *          排产单id
     * @param stationId
     *          工位id
     * @return
     */
    StationInfoModel getStationInfo(String scheduleId, String stationId,String processId);

    /**
     * 获取工单完工数量
     * @param scheduleId
     * @param scheduleIds
     * @return
     */
    Integer getOutPutQtys(String scheduleId, List<String> scheduleIds);

    /**
     * 获取工单完工数量
     * @param scheduleIds
     * @param outputProcessId
     * @return
     */
    Integer getOutPutQtys(List<String> scheduleIds, String outputProcessId);

    /**
     * 获取机台产量（注塑成型工序的产量，此方法成为历史，不要调用）
     * @param scheduleIds
     * @param outputProcessId
     * @return
     */
    @Deprecated
    Integer getMachineOutputQty(List<String> scheduleIds, String outputProcessId);

    /**
     * 获取单个排产单完工数（已排除不良********常用方法）
     * @param scheduleId
     * @return
     */
    Integer getOutPutQtys(String scheduleId);
    /**
     * 获取工序的完成量（工序的产量**************常用方法）
     * @param scheduleId
     * @param baseProcess
     * @return
     */
    Integer getOutputQtyForProcess(String scheduleId, BaseProcess baseProcess);

    /**
     * 获取机台产量（注塑成型工序的产量）
     * @param scheduleId
     * @return
     */
    /*Integer getMachineOutputQty(String scheduleId);*/

    /**
     * 获取机台产量（注塑成型工序的产量***************常用方法）
     * @param scheduleId
     *          机台关联的排产单
     * @param processId
     *          注塑成型工序
     * @return
     */
    Integer getMachineOutputQty(String scheduleId,String processId);

    /**
     * 获取工位完成量
     * @param scheduleId
     * @param stationId
     * @param processId
     * @param machineId
     * @param moDescForStationFail
     * @return
     */
    Integer getOutputQtyForStation(String scheduleId, String stationId, String processId,String machineId, MoDescInfoModel moDescForStationFail);
}
