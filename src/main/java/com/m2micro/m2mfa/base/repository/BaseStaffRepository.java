package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 员工（职员）表 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseStaffRepository extends BaseRepository<BaseStaff,String> {

}