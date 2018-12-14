package com.m2micro.m2mfa.common.util;

/*import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.SpringContextUtil;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.*;
import org.flowable.engine.impl.ProcessEngineImpl;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.validation.ValidationError;*/

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/14 09:22
 * @Description:
 */
public class FlowableXmlUtil {

    /**
     * 创建用户任务
     * @param id
     * @param name
     * @param assignee
     * @return
     *//*
    public static UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    *//**
     * 创建连线（两个任务节点之间的连线）
     * @param from
     *      起始
     * @param to
     *      终止
     * @return  bpmn标准连线
     *//*
    public static SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    *//**
     * 创建开始事件
     * @return  开始事件
     *//*
    public static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    *//**
     * 创建结束事件
     * @return 结束事件
     *//*
    public static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

    *//**
     * 自动画图（填充坐标）
     * @param model
     *      bpmn标准模型
     *//*
    public static void autoLayout(BpmnModel model){
        new BpmnAutoLayout(model).execute();
    }

    *//**
     * 校验bpmn标准模型
     * @param model
     *      bpmn标准模型
     *//*
    public static void validateModel(BpmnModel model){
        ProcessEngineImpl processEngine = (ProcessEngineImpl)SpringContextUtil.getBean("processEngine");
        ProcessEngineConfigurationImpl processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        List<ValidationError> errors = processEngineConfiguration.getProcessValidator().validate(model);
        if(errors!=null&&errors.size()>0){
            throw new MMException("流程定义有错误！");
        }
    }*/

}
