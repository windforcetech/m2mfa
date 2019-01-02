package com.m2micro.m2mfa.pad.operate;

public interface BaseOperate {

    /**
     * 上工
     * @param obj
     * @return
     */
    Object startWork(Object obj);

    /**
     * 下工
     * @param obj
     * @return
     */
    Object stopWork(Object obj);

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
