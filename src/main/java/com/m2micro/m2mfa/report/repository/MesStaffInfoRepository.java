package com.m2micro.m2mfa.report.repository;

import com.m2micro.m2mfa.report.entity.MesStaffInfo;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 工单职员信息 Repository 接口
 * @author liaotao
 * @since 2019-06-13
 */
@Repository
public interface MesStaffInfoRepository extends BaseRepository<MesStaffInfo,String> {

}