package com.m2micro.m2mfa.record.repository;

import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 人员作业记录 Repository 接口
 * @author wanglei
 * @since 2018-12-27
 */
@Repository
public interface MesRecordStaffRepository extends BaseRepository<MesRecordStaff,String> {

}