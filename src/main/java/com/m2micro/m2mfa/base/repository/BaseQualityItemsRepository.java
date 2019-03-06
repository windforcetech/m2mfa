package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验项目 Repository 接口
 * @author liaotao
 * @since 2019-01-28
 */
@Repository
public interface BaseQualityItemsRepository extends BaseRepository<BaseQualityItems,String> {
    /**
     * 校验编码
     * @return
     */
    List<BaseQualityItems> findByItemCodeAndItemIdNot(String itemCode,String itemId);

    /**
     * 校验名称
     * @return
     */
    List<BaseQualityItems> findByItemNameAndItemIdNot(String itemCode,String itemId);

    /**
     * 有效
     * @param enabled
     * @return
     */
    List<BaseQualityItems> findByEnabled(Boolean enabled);
}