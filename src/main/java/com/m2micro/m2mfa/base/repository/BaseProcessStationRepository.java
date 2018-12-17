package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 工序工位关系 Repository 接口
 * @author chenshuhong
 * @since 2018-12-14
 */
@Repository
public interface BaseProcessStationRepository extends BaseRepository<BaseProcessStation,String> {

}