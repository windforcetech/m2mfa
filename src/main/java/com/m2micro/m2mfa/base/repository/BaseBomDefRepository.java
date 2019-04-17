package com.m2micro.m2mfa.base.repository;

import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseBomDef;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Bom def Repository 接口
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Repository
public interface BaseBomDefRepository extends BaseRepository<BaseBomDef, String> {

    void deleteAllByBomIdIsIn(List<String> ids);

    List<BaseBomDef> findAllByBomId(String bomid);
}
