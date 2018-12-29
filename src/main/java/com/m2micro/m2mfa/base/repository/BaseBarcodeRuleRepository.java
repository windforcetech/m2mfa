package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 条形码主表定义 Repository 接口
 * @author wanglei
 * @since 2018-12-29
 */
@Repository
public interface BaseBarcodeRuleRepository extends BaseRepository<BaseBarcodeRule,String> {


    int countByRuleCode(String ruleCode);

    int countByRuleCodeAndIdNot(String ruleCode,String id);
    void deleteByIdIn(List<String> ids);
}