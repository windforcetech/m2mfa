package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 生产途程单身 Repository 接口
 * @author chenshuhong
 * @since 2018-12-17
 */
@Repository
public interface BaseRouteDefRepository extends BaseRepository<BaseRouteDef,String> {


    @Query("select r.routeDefId  from  BaseRouteDef  as r  where r.processId =?1 ")
    List<String> selectoneprocessId(String processId);

    @Transactional
    @Modifying
    @Query("delete  from  BaseRouteDef  as r  where r.routeId =?1 ")
     void deleterouteId(String routeId);

    @Query(value = "SELECT\n" +
                    "	brd.process_id \n" +
                    "FROM\n" +
                    "	mes_part_route mpr,\n" +
                    "	base_route_def brd \n" +
                    "WHERE\n" +
                    "	mpr.route_id = brd.route_id \n" +
                    "	AND mpr.part_route_id = ?1 \n" +
                    "	AND brd.nextprocess_id = ?2", nativeQuery = true)
    String getBeforeProcessId(String partRouteId,String processId );

    @Query(value = "SELECT\n" +
                    "	brd.nextprocess_id \n" +
                    "FROM\n" +
                    "	mes_part_route mpr,\n" +
                    "	base_route_def brd \n" +
                    "WHERE\n" +
                    "	mpr.route_id = brd.route_id \n" +
                    "	AND mpr.part_route_id = ?1 \n" +
                    "	AND brd.process_id = ?2", nativeQuery = true)
    String getNextProcessId(String partRouteId,String processId );

}