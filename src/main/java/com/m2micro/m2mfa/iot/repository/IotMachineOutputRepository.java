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
     * 通过物业id查找机台产出信息
     * @param orgId
     *          物业id
     * @return  机台产出信息
     */
    IotMachineOutput findIotMachineOutputByOrgId(String orgId);

    /**
     * 通过物业id删除
     * @param orgId
     * @return
     */
    Integer deleteByOrgId(String orgId);
}