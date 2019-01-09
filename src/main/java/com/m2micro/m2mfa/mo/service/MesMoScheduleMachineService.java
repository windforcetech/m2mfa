package com.m2micro.m2mfa.mo.service;

import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.mo.model.MesMoScheduleMachineModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleMachineQuery;

import java.util.List;

/**
 * 排产机台服务
 */
public interface MesMoScheduleMachineService {

    /**
     * 获取所有已经绑定的机台及部门
     * @return  获取所有已经绑定的机台及部门
     */
    TreeNode getAllDepartAndMachine();

    /**
     * 获取排程机台页面的排产单
     * @param machineId
     *          机台id
     * @return  排产单信息
     */
    List<MesMoScheduleModel> getScheduleForScheduleMachine(String machineId);

    /**
     * 排产机台列表
     * @param query
     *          查询参数
     * @return  排产机台列表
     */
    PageUtil<MesMoScheduleMachineModel> list(MesMoScheduleMachineQuery query);
}
