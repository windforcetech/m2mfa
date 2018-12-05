package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 料件基本资料 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BasePartsRepository extends BaseRepository<BaseParts,String> {

    /**
     * 校验partNo料件编号
     * @param partNo
     * @param partId
     * @return
     */
    List<BaseParts> findByPartNoAndPartIdNot(String partNo,String partId);
}