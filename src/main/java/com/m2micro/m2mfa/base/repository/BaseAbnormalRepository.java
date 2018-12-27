package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseAbnormal;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 异常项目档 Repository 接口
 * @author liaotao
 * @since 2018-12-27
 */
@Repository
public interface BaseAbnormalRepository extends BaseRepository<BaseAbnormal,String> {

}