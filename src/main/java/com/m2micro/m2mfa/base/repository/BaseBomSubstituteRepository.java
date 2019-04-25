package com.m2micro.m2mfa.base.repository;

import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseBomDef;
import com.m2micro.m2mfa.base.entity.BaseBomSubstitute;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Bom def Repository 接口
 *
 * @author liaotao
 * @since 2019-01-04
 */
@Repository
public interface BaseBomSubstituteRepository extends BaseRepository<BaseBomSubstitute, String> {

     void deleteAllByBomIdIsIn(List<String> ids);
     List<BaseBomSubstitute> findAllByBomId(String bomid);

     /**
      * 获取料件物料清单取替代关联
      * @param partId
      *             料件id
      * @return
      */
     List<BaseBomSubstitute> findByPartIdAndGroupId(String partId,String groupId);
}
