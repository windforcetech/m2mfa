package com.m2micro.m2mfa.pad.operate;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.model.StartWorkPara;
import com.m2micro.m2mfa.pad.model.StopWorkModel;
import com.m2micro.m2mfa.pad.model.StopWorkPara;

/**
 * 基础操作接口
 */
public interface BaseOperate {

    /**
     * 获取操作栏相关信息
     * @param scheduleId
     * @param stationId
     */
    OperationInfo getOperationInfo(String scheduleId, String stationId);

    /**
     * 上工
     * @param obj
     * @return
     */
    StartWorkPara startWork(PadPara obj);

    /**
     * 下工
     * @param obj
     * @return
     */
    StopWorkModel stopWork(StopWorkPara obj);

    /**
     * 结束作业
     * @param obj
     * @return
     */
    Object finishHomework(Object obj);
    /**
     * 不良品数
     * @param obj
     * @return
     */
    Object defectiveProducts (Object obj);
    /**
     * 提报异常
     * @param obj
     * @return
     */
    Object reportingAnomalies(Object obj);
    /**
     * 作业输入
     * @param obj
     * @return
     */
    Object jobInput(Object obj);

    /**
     * 作业指导
     * @param obj
     * @return
     */
    Object homeworkGuidance(Object obj);

    /**
     * 操作历史
     * @param obj
     * @return
     */
    Object operationHistory (Object obj);


}
