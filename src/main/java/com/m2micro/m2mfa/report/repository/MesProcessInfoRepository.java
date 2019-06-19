package com.m2micro.m2mfa.report.repository;

import com.m2micro.m2mfa.report.entity.MesProcessInfo;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 工单工序信息 Repository 接口
 * @author chenshuhong
 * @since 2019-06-12
 */
@Repository
public interface MesProcessInfoRepository extends BaseRepository<MesProcessInfo,String> {

}