package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工单料表 Repository 接口
 * @author liaotao
 * @since 2019-03-29
 */
@Repository
public interface MesMoBomRepository extends BaseRepository<MesMoBom,String> {
    /**
     * 通过工单id删除工单料表信息
     * @param moId
     * @return
     */
    Integer deleteAllByMoId(String moId);

    /**
     * 删除工单料表信息
     * @param ids
     * @return
     */
    Integer deleteAllByMoIdIn(List<String> ids);

    /**
     * 获取工单料表信息
     * @param moId
     * @return
     */
    List<MesMoBom> findByMoId(String moId);
}