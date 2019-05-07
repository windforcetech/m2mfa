package com.m2micro.m2mfa.pr.repository;

import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 料件途程设定工序 Repository 接口
 * @author liaotao
 * @since 2018-12-19
 */
@Repository
public interface MesPartRouteProcessRepository extends BaseRepository<MesPartRouteProcess,String> {

    @Transactional
    @Modifying
    @Query(value="delete from mes_part_route_process where  partrouteid=?1",nativeQuery = true)
    void deleteParRouteID(String parrouteid);

    /**
     * 获取途程的第一个扫描工序id
     * @param itemValue
     * @param partRouteId
     * @return
     */
    @Query(value = "SELECT\n" +
                    "	mprp.processid\n" +
                    "FROM\n" +
                    "	mes_part_route mpr,\n" +
                    "	mes_part_route_process mprp,\n" +
                    "	base_process bp,\n" +
                    "	base_items_target bit\n" +
                    "WHERE mpr.part_route_id = mprp.partrouteid\n" +
                    "AND mprp.processid = bp.process_id\n" +
                    "AND bp.collection = bit.id\n" +
                    "AND bit.item_value=?1\n" +
                    "AND mpr.part_route_id=?2\n" +
                    "ORDER BY mprp.setp ASC limit 1",nativeQuery = true)
    String getFirstScanProcessId(String itemValue,String partRouteId);

    /**
     * 获取上一工序信息
     * @param partrouteid
     * @param nextprocessid
     * @return
     */
    //MesPartRouteProcess findByPartrouteidAndNextprocessid(String partrouteid,String nextprocessid);

    /**
     * 获取下一工序
     * @param partrouteid
     * @param nextprocessid
     * @return
     */
    //MesPartRouteProcess findByPartrouteidAndProcessid(String partrouteid,String nextprocessid);

    /**
     * 获取工序信息
     * @param partrouteid
     * @param processid
     * @return
     */
    MesPartRouteProcess findByPartrouteidAndProcessid(String partrouteid,String processid);

    @Query(value = "SELECT\n" +
            "	mprp.process_id \n" +
            "FROM\n" +
            "	mes_part_route_process mprp \n" +
            "WHERE\n" +
            "	mprp.part_route_id = ?1 \n" +
            "	AND brd.next_process_id = ?2", nativeQuery = true)
    String getBeforeProcessId(String partRouteId,String processId );

    @Query(value = "SELECT\n" +
            "	mprp.next_process_id \n" +
            "FROM\n" +
            "	mes_part_route_process mprp \n" +
            "WHERE\n" +
            "	mprp.part_route_id = ?1 \n" +
            "	AND brd.process_id = ?2", nativeQuery = true)
    String getNextProcessId(String partRouteId,String processId );
}
