package com.m2micro.m2mfa.base.repository;

import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Bom desc Repository 接口
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Repository
public interface BaseBomDescRepository extends BaseRepository<BaseBomDesc, String> {

    List<BaseBomDesc> findAllByPartId(String partid);

}
