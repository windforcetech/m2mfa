package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生产途程单头 Repository 接口
 * @author chenshuhong
 * @since 2018-12-17
 */
@Repository
public interface BaseRouteDescRepository extends BaseRepository<BaseRouteDesc,String> {

    @Query("select r.routeId  from BaseRouteDesc  as r where r.routeNo=?1")
    String selectRouteNo(String routeNo);

}