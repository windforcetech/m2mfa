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


}
