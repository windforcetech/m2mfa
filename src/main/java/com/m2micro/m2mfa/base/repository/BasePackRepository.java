package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 包装 Repository 接口
 * @author wanglei
 * @since 2018-12-27
 */
@Repository
public interface BasePackRepository extends BaseRepository<BasePack,String> {

}