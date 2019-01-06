package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.ScheduleSequenceModel;

import java.util.List;

/**
 * 排产单调度 服务类
 * @author liaotao
 * @since 2018-01-04
 */
public interface MesMoScheduleDispatchService {
    /**
     * 获取所有已经绑定的机台及部门
     * @return  获取所有已经绑定的机台及部门
     */
    TreeNode getAllDepartAndMachine();

    /**
     * 获取调度页面的排产单
     * @return  排产单
     */
    List<MesMoScheduleModel> getScheduleDispatch(String machineId);

    /**
     * 更新排产单顺序
     * @param scheduleSequenceModels
     *          排产单顺序信息
     */
    void updateSequence(ScheduleSequenceModel[] scheduleSequenceModels);

}
