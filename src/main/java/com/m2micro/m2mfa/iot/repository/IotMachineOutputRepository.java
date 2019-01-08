package com.m2micro.m2mfa.iot.repository;

import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 机台产出信息 Repository 接口
 * @author liaotao
 * @since 2019-01-08
 */
@Repository
public interface IotMachineOutputRepository extends BaseRepository<IotMachineOutput,String> {
    /**
     * 通过机台id查找机台产出信息
     * @param machineId
     *          机台id
     * @return  机台产出信息
     */
    IotMachineOutput findIotMachineOutputByMachineId(String machineId);

    /**
     * 通过机台id删除
     * @param machineId
     * @return
     */
    Integer deleteByMachineId(String machineId);
}