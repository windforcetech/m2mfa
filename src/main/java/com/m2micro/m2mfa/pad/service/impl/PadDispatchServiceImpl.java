package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.SpringContextUtil;
import com.m2micro.m2mfa.pad.constant.PadDispatchConstant;
import com.m2micro.m2mfa.pad.model.PadPara;
import com.m2micro.m2mfa.pad.service.PadDispatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Auther: liaotao
 * @Date: 2019/1/14 17:17
 * @Description:
 */
@Service
public class PadDispatchServiceImpl implements PadDispatchService {
    @Override
    public Object startWork(PadPara obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = PadDispatchConstant.getHandle(obj.getStationId());
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的上工操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("startWork",PadPara.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object stopWork(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("stopWork",Object.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object finishHomework(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("finishHomework",Object.class);
        return method.invoke(handleInstance,obj);
    }

    @Override
    public Object defectiveProducts(Object obj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String handle = "";
        if(StringUtils.isEmpty(handle)){
            throw new MMException("工位没有对应的操作！");
        }
        Class<?> clazz = Class.forName(handle);
        Object handleInstance = SpringContextUtil.getBean(clazz);
        Method method = clazz.getMethod("defectiveProducts",Object.class);
        return method.invoke(handleInstance,obj);
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
