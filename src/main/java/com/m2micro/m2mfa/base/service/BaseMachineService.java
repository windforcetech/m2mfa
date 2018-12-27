package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BaseMachineQuery;

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
}