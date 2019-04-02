package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseAlert;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 预警方式设定 Repository 接口
 * @author chenshuhong
 * @since 2019-04-02
 */
@Repository
public interface BaseAlertRepository extends BaseRepository<BaseAlert,String> {

}