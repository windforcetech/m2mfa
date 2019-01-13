package com.m2micro.m2mfa.iot.service;

import com.m2micro.framework.starter.entity.OrgAndDeviceNode;
import com.m2micro.iot.client.model.DeviceData;
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
     * 处理iot平台推送过来的数据
     * @param deviceData
     *          iot平台推送过来的数据
     */
    void handleDeviceData(DeviceData deviceData);
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<IotMachineOutput> list(Query query);

    /**
     * 通过机台id删除
     * @param orgId
     * @return
     */
    Integer deleteByOrgId(String orgId);

    /**
     * 通过机台ids删除
     * @param orgIds
     * @return
     */
    void deleteByOrgIds(String[] orgIds);

    /**
     * 通过机台id查找机台产量信息
     * @param machineId
     *          机台id
     * @return  机台产量信息
     */
    IotMachineOutput findIotMachineOutputByMachineId(String machineId);

    /**
     * 获取物业和拓扑的关联关系
     * @param deviceId
     * @return
     */
    OrgAndDeviceNode getOrgAndDeviceNodeByDeviceId(String deviceId);
}