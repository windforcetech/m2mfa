package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工排班表 Repository 接口
 * @author liaotao
 * @since 2019-01-04
 */
@Repository
public interface BaseStaffshiftRepository extends BaseRepository<BaseStaffshift,String> {

    int countByStaffIdIn(String[] staffId);

    List<BaseStaffshift> findByStaffId(String staffId);
}
