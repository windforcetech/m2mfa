package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 标签模板 Repository 接口
 * @author liaotao
 * @since 2019-01-22
 */
@Repository
public interface BaseTemplateRepository extends BaseRepository<BaseTemplate,String> {

    int countByNumber(String number);
    int countByNumberAndIdNot(String number,String id);
    void  deleteByIdIn(String[] templateIds);
}