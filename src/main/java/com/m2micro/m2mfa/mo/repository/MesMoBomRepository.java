package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 工单料表 Repository 接口
 * @author liaotao
 * @since 2019-03-29
 */
@Repository
public interface MesMoBomRepository extends BaseRepository<MesMoBom,String> {

}