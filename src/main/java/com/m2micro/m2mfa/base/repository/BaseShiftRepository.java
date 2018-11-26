package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 班别基本资料 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseShiftRepository extends BaseRepository<BaseShift,String> {

}