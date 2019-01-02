package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordWipRec;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 在制记录表 Repository 接口
 * @author liaotao
 * @since 2019-01-02
 */
@Repository
public interface MesRecordWipRecRepository extends BaseRepository<MesRecordWipRec,String> {

}