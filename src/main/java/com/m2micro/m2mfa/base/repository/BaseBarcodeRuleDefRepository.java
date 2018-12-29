package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 条形码子序列字段定义 Repository 接口
 * @author wanglei
 * @since 2018-12-29
 */
@Repository
public interface BaseBarcodeRuleDefRepository extends BaseRepository<BaseBarcodeRuleDef,String> {

    int countByNameAndBarcodeId(String name,String barcodeId);
    int countByNameAndBarcodeIdAndIdNot(String name,String barcodeId,String id);
    void deleteByBarcodeIdIn(List<String> barcodeIds);
    List<BaseBarcodeRuleDef> findByBarcodeId(String barcodeId);
}