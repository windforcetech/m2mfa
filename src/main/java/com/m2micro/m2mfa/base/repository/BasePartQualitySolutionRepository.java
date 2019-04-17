package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePartQualitySolution;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件品质方案关联 Repository 接口
 * @author liaotao
 * @since 2019-03-06
 */
@Repository
public interface BasePartQualitySolutionRepository extends BaseRepository<BasePartQualitySolution,String> {
    /**
     * 查找
     * @param solutionId
     * @return
     */
    List<BasePartQualitySolution> findBySolutionId(String solutionId);

    /**
     * 获取料件品质方案关联
     * @param partId
     * @return
     */
    BasePartQualitySolution findByPartId(String partId);
}