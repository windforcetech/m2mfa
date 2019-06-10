package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.node.SelectNode;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;
import com.m2micro.m2mfa.mo.query.MesMachineQuery;

import java.util.List;

/**
 * 机台主档 服务类
 * @author liaotao
 * @since 2018-11-22
 */
public interface BaseMachineService extends BaseService<BaseMachine,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseMachine> list(BaseMachineQuery query);
    /**
     * 根据编号查找机台
     * @param code
     * @return
     */
    List<BaseMachine> findAllByCode(String code);

    /**
     * 校验code
     * @param code
     * @param machineId
     * @return
     */
    List<BaseMachine> findByCodeAndMachineIdNot(String code ,String machineId);

    /**
     * 获取排产单的机台信息
     * @return
     */
    PageUtil<BaseMachine>   findbyMachine( MesMachineQuery mesMachineQuery);

    /**
     * 保存机台信息
     * @param baseMachine
     * @return
     */
    BaseMachine saveEntity(BaseMachine baseMachine);

    /**
     * 删除机台信息
     * @param ids
     */
    ResponseMessage delete(String[] ids);

    /**
     * 获取机台名称下拉选项
     * @return 获取机台名称下拉选项
     */
    List<SelectNode> getNames(String machineId);

    /**
     * 判断该部门下面是否有关联机台
     * @param departmentId
     * @return
     */
    boolean isMachineandDepartment(String departmentId);

    /**
     * 更新机台状态
     * @param flag
     *          状态
     * @param machineId
     *          机台id
     * @return
     */
    Integer setFlagFor(String flag, String machineId);

    /**
     * 更新机台状态为生产
     * @param machineId
     *          机台id
     */
    void setFlagForProduce(String machineId);

    /**
     * 更新机台状态为停机
     * @param machineId
     *          机台id
     */
    void setFlagForStop(String machineId);
}
