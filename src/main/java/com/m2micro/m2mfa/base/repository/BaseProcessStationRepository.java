package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * 工序工位关系 Repository 接口
 * @author chenshuhong
 * @since 2018-12-14
 */
@Repository
public interface BaseProcessStationRepository extends BaseRepository<BaseProcessStation,String> {

    @Query("delete from  BaseProcessStation as p  where p.processId=?1")
    @Transactional
    @Modifying
     void deleteprocessId(String processId);
}