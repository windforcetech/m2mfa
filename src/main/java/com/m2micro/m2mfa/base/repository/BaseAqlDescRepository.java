package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseAqlDesc;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 抽样标准(aql)-主档 Repository 接口
 * @author liaotao
 * @since 2019-01-29
 */
@Repository
public interface BaseAqlDescRepository extends BaseRepository<BaseAqlDesc,String> {
    /**
     * 有效的
     * @param enabled
     * @return
     */
    List<BaseAqlDesc> findByEnabled(Boolean enabled);
}