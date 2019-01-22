package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工序基本档 Repository 接口
 * @author chenshuhong
 * @since 2018-12-14
 */
@Repository
public interface BaseProcessRepository extends BaseRepository<BaseProcess,String> {

    @Query("select p.processId from BaseProcess  as p  where p.processCode=?1")
     String selectprocessCode(String processCode);

    @Query(value = "select process_id from base_process  where process_name=?1", nativeQuery = true)
    String selectName(String processName);

}
