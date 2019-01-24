package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 不良現象代碼 Repository 接口
 * @author chenshuhong
 * @since 2019-01-24
 */
@Repository
public interface BaseDefectRepository extends BaseRepository<BaseDefect,String> {

}
