package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 在制记录表历史 Repository 接口
 * @author liaotao
 * @since 2019-03-27
 */
@Repository
public interface MesRecordWipLogRepository extends BaseRepository<MesRecordWipLog,String> {

}