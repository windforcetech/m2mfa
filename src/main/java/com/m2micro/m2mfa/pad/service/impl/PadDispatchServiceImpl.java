package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.SpringContextUtil;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.service.BaseStationService;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.constant.PadDispatchConstant;
import com.m2micro.m2mfa.pad.model.*;
import com.m2micro.m2mfa.pad.service.PadDispatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Auther: liaotao
 * @Date: 2019/1/14 17:17
 * @Description:
 */
@Service
public class PadDispatchServiceImpl implements PadDispatchService {
    @Autowired
    MesMoScheduleService mesMoScheduleService;

    @Autowired
    BaseStationService baseStationService;

    @Override
    public OperationInfo getOperationInfo(String scheduleId, String stationId,String processId) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if(!mesMoScheduleService.isScheduleFlag(scheduleId)){
            throw  new MMException("排产单状态不可执行");
        }
        BaseStation baseStation = baseStationService.findById(stationId).orElse(null);
        String handle = PadDispatchConstant.getHandle(baseStation.getCode());
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("getOperationInfo",String.class,String.class,String.class);
        return (OperationInfo)method.invoke(handleInstance,scheduleId,stationId,processId);
    }


    @Override
    public StartWorkPara startWork(PadPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!mesMoScheduleService.isScheduleFlag(obj.getScheduleId())){
            throw  new MMException("排产单状态不可执行");
        }
        BaseStation baseStation = baseStationService.findById(obj.getStationId()).orElse(null);
        String handle = PadDispatchConstant.getHandle(baseStation.getCode());
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("startWork",PadPara.class);
        return (StartWorkPara)method.invoke(handleInstance,obj);
    }


    @Override
    public StopWorkModel stopWork(StopWorkPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BaseStation baseStation = baseStationService.findById(obj.getStationId()).orElse(null);
        String handle = PadDispatchConstant.getHandle(baseStation.getCode());
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("stopWork",StopWorkPara.class);
        return (StopWorkModel)method.invoke(handleInstance,obj);
    }


    @Override
    public FinishHomeworkModel finishHomework(FinishHomeworkPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!mesMoScheduleService.isScheduleFlag(obj.getScheduleId())){
            throw  new MMException("排产单状态不可执行");
        }
        BaseStation baseStation = baseStationService.findById(obj.getStationId()).orElse(null);
        String handle = PadDispatchConstant.getHandle(baseStation.getCode());
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("finishHomework",FinishHomeworkPara.class);
        return (FinishHomeworkModel)  method.invoke(handleInstance,obj);
    }


    @Override
    public Object defectiveProducts(Padbad obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException ,MMException{

        BaseStation baseStation = baseStationService.findById(obj.getStationId()).orElse(null);
        String handle = PadDispatchConstant.getHandle(baseStation.getCode());
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        //Method method = clazz.getMethod("defectiveProducts",Padbad.class);
        //return method.invoke(handleInstance,obj);
        try {
            Method method = clazz.getMethod("defectiveProducts",Padbad.class);
            return method.invoke(handleInstance,obj);
        }catch (InvocationTargetException ex){
            Throwable t = ex.getTargetException();
            if(t instanceof MMException){
                throw (MMException)t;
            }
            throw ex;
        }

    }

    @Override
    public Object reportingAnomalies(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("reportingAnomalies",Object.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object jobInput(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("jobInput",Object.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object homeworkGuidance(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("homeworkGuidance",Object.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object operationHistory(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("operationHistory",Object.class);
        return method.invoke(handleInstance,obj);
    }
}


