package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 生产排程表表头 Repository 接口
 * @author liaotao
 * @since 2018-12-26
 */
@Repository
public interface MesMoScheduleRepository extends BaseRepository<MesMoSchedule,String> {

}