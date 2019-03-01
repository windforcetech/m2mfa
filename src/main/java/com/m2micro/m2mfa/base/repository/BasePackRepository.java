package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.framework.commons.BaseRepository;
import com.m2micro.m2mfa.base.entity.BaseParts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 包装 Repository 接口
 * @author wanglei
 * @since 2018-12-27
 */
@Repository
public interface BasePackRepository extends BaseRepository<BasePack,String> {

    int countByPartIdAndCategory(String partId,Integer category);
    int countByIdNotAndPartIdAndCategory(String id,String partId,Integer category);
    List<BasePack> findByPartId(String partId);
    List<BasePack> findByPartIdIn(List<String> partIds);

}
