package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWipFail;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 在制不良记录表 Repository 接口
 * @author chenshuhong
 * @since 2019-05-06
 */
@Repository
public interface MesRecordWipFailRepository extends BaseRepository<MesRecordWipFail,String> {

}