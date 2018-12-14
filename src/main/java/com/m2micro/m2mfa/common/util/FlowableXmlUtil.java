package com.m2micro.m2mfa.common.util;

import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.*;

/**
 * @Auther: liaotao
 * @Date: 2018/12/14 09:22
 * @Description:
 */
public class FlowableXmlUtil {
    public static UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    public static SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    public static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    /**
     * 创建结束事件
     * @return 结束事件
     */
    public static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

    /**
     * 自动画图（填充坐标）
     * @param model
     */
    public static void autoLayout(BpmnModel model){
        new BpmnAutoLayout(model).execute();
    }
}
