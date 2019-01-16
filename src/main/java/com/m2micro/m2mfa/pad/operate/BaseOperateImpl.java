package com.m2micro.m2mfa.pad.operate;

import com.m2micro.m2mfa.pad.model.PadPara;

/**
 * @Auther: liaotao
 * @Date: 2019/1/2 10:31
 * @Description:
 */
public class BaseOperateImpl implements BaseOperate {

    @Override
    public Object startWork(PadPara obj) {
        System.out.println("+++++++++++++++++++");
        return null;
    }

    @Override
    public Object stopWork(Object obj) {
        return null;
    }

    @Override
    public Object finishHomework(Object obj) {
        return null;
    }

    @Override
    public Object defectiveProducts(Object obj) {
        return null;
    }

    @Override
    public Object reportingAnomalies(Object obj) {
        return null;
    }

    @Override
    public Object jobInput(Object obj) {
        return null;
    }

    @Override
    public Object homeworkGuidance(Object obj) {
        return null;
    }

    @Override
    public Object operationHistory(Object obj) {
        return null;
    }
}
