package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 机台主档 Repository 接口
 * @author liaotao
 * @since 2018-11-22
 */
@Repository
public interface BaseMachineRepository extends BaseRepository<BaseMachine,String> {

    /**
     * 根据编号查找机台
     * @param code
     * @return
     */
    List<BaseMachine> findAllByCodeAndGroupId(String code,String groupId);

    /**
     * 校验code
     * @param code
     * @param machineId
     * @return
     */
    List<BaseMachine> findByCodeAndGroupIdAndMachineIdNot(String code,String groupId,String machineId);

    @Query(value = "SELECT bm.* FROM base_machine bm WHERE bm.id = ?1",nativeQuery = true)
    BaseMachine findByOrgId(String orgId);

    @Query(value = "SELECT bm.* FROM base_machine bm WHERE bm.id = ?1 AND bm.group_id = ?2",nativeQuery = true)
    List<BaseMachine> findByOrgIds(String orgId,String groupId);
}
