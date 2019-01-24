package com.m2micro.m2mfa.pad.service;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.pad.model.*;

import java.lang.reflect.InvocationTargetException;

public interface PadDispatchService {

    /**
     * 获取操作栏相关信息
     * @param scheduleId
     * @param stationId
     */
    OperationInfo getOperationInfo(String scheduleId, String stationId) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 上工
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    StartWorkPara startWork(PadPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 下工
     * @param obj
     * @return
     */
    StopWorkModel stopWork(StopWorkPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 结束作业
     * @param obj
     * @return
     */
    Object finishHomework(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    /**
     * 不良品数
     * @param obj
     * @return
     */
    Object defectiveProducts (Padbad obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    /**
     * 提报异常
     * @param obj
     * @return
     */
    Object reportingAnomalies(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    /**
     * 作业输入
     * @param obj
     * @return
     */
    Object jobInput(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 作业指导
     * @param obj
     * @return
     */
    Object homeworkGuidance(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 操作历史
     * @param obj
     * @return
     */
    Object operationHistory (Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
