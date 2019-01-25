package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 模板变量 Repository 接口
 * @author liaotao
 * @since 2019-01-22
 */
@Repository
public interface BaseTemplateVarRepository extends BaseRepository<BaseTemplateVar,String> {

    int countByTemplateIdAndName(String templateId,String name);
    int countByIdNotAndTemplateIdAndName(String varId,String templateId,String name);

    void deleteByTemplateId(String templateId);
    List<BaseTemplateVar> findByTemplateId(String templateId);

    void deleteByTemplateIdIn(String[] templateIds);
}