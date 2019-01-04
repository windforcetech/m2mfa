package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.base.node.TreeNode;

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

}
