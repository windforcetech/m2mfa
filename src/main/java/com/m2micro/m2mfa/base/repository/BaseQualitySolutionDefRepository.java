package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验方案明细 Repository 接口
 * @author liaotao
 * @since 2019-01-28
 */
@Repository
public interface BaseQualitySolutionDefRepository extends BaseRepository<BaseQualitySolutionDef,String> {
    /**
     * 根据检验项目主键查找检验方案明细
     * @param qitemId
     *          检验项目主键
     * @return  检验方案明细
     */
    List<BaseQualitySolutionDef> findByQitemId(String qitemId);

    /**
     * 根据检验方案主键查找检验方案明细
     * @param solutionId
     *              检验方案主档id
     * @return      检验方案明细
     */
    List<BaseQualitySolutionDef> findBySolutionId(String solutionId);
}