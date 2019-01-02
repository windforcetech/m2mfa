package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工单主档 Repository 接口
 * @author liaotao
 * @since 2018-12-10
 */
@Repository
public interface MesMoDescRepository extends BaseRepository<MesMoDesc,String> {
    /**
     * 根据物料编号查找工单主档
     * @param partId
     * @return
     */
    List<MesMoDesc> findByPartId(String partId);

    /**
     * 根据客户id查找工单主档
     * @param customerId
     * @return
     */
    List<MesMoDesc> findByCustomerId(String customerId);

    /**
     * 根据编号查找工单主档
     * @param moNumber
     *          编号
     * @param moId
     *          主键
     * @return
     */
    List<MesMoDesc> findByMoNumberAndMoIdNot(String moNumber,String moId);

    /**
     * 更新工单状态
     * @param closeFlag
     *          状态
     * @param moId
     *          工单id
     * @return
     */
    @Modifying
    @Query("update MesMoDesc m set m.closeFlag = ?1 where m.moId = ?2")
    Integer setCloseFlagFor(Integer closeFlag, String moId);
    /**
     * 更新工单状态及冻结前状态
     * @param closeFlag
     *          状态
     * @param moId
     *          工单id
     * @return
     */
    @Modifying
    @Query("update MesMoDesc m set m.closeFlag = ?1 , m.prefreezingState = ?2 where m.moId = ?3")
    Integer setCloseFlagAndPrefreezingStateFor(Integer closeFlag,Integer prefreezingState, String moId);


}