package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * 生产途程单身 Repository 接口
 * @author chenshuhong
 * @since 2018-12-17
 */
@Repository
public interface BaseRouteDefRepository extends BaseRepository<BaseRouteDef,String> {

    @Query("select r.routeDefId  from  BaseRouteDef  as r  where r.processId =?1 ")
    public String  selectoneprocessId(String processId);
}