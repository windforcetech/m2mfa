package com.m2micro.m2mfa.mo.repository;

import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工单主档 Repository 接口
 * @author liaotao
 * @since 2018-11-30
 */
@Repository
public interface MesMoDescRepository extends BaseRepository<MesMoDesc,String> {

    /**
     * 根据物料编号查找工单主档
     * @param partNo
     * @return
     */
    List<MesMoDesc> findByPartNo(String partNo);

}