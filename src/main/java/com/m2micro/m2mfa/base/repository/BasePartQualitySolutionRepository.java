package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePartQualitySolution;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件品质方案关联 Repository 接口
 * @author liaotao
 * @since 2019-03-05
 */
@Repository
public interface BasePartQualitySolutionRepository extends BaseRepository<BasePartQualitySolution,String> {
    /**
     * 根据solutionId查找料件品质方案关联
     * @param solutionId
     * @return
     */
    List<BasePartQualitySolution> findBySolutionId(String solutionId);
}