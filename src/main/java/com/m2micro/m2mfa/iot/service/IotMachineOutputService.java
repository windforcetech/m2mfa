package com.m2micro.m2mfa.iot.service;

import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 机台产出信息 服务类
 * @author liaotao
 * @since 2019-01-08
 */
public interface IotMachineOutputService extends BaseService<IotMachineOutput,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<IotMachineOutput> list(Query query);

    /**
     * 通过机台id删除
     * @param machineId
     * @return
     */
    Integer deleteByMachineId(String machineId);

    /**
     * 通过机台ids删除
     * @param machineIds
     * @return
     */
    void deleteByMachineIds(String[] machineIds);
}