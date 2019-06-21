package com.m2micro.m2mfa.report.repository;

import com.m2micro.m2mfa.report.entity.MesInfo;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 工单信息 Repository 接口
 * @author liaotao
 * @since 2019-06-12
 */
@Repository
public interface MesInfoRepository extends BaseRepository<MesInfo,String> {

}