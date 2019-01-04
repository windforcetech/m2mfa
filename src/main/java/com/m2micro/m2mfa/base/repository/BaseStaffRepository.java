package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工（职员）表 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseStaffRepository extends BaseRepository<BaseStaff,String> {

    List<BaseStaff> findByCodeAndStaffIdNot(String code,String staffId);
   // List<BaseStaff> findByCodeOrStaffNameOrdOrDutyIdIn(String code,String staffName,List<String> dutyIds);
    @Query(value = "select * from base_staff where  `code` =?1", nativeQuery = true)
    BaseStaff finydbStaffNo(String code);
}