package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseMold;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 模具主档 Repository 接口
 * @author liaotao
 * @since 2018-11-26
 */
@Repository
public interface BaseMoldRepository extends BaseRepository<BaseMold,String> {

    /**
     * 根据编号查找模具
     * @param code
     * @return
     */
    List<BaseMold> findAllByCode(String code);

    /**
     * 校验code唯一性
     * @param code
     * @param moldId
     * @return
     */
    List<BaseMold> findByCodeAndMoldIdNot(String code, String moldId);

    /**
     * 获取有效模具
     * @param enabled
     * @return
     */
    List<BaseMold> findByEnabled(boolean enabled);
}
