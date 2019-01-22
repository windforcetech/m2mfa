package com.m2micro.m2mfa.base.repository;

import com.m2micro.m2mfa.base.entity.BaseTemplateVar;
import com.m2micro.framework.commons.BaseRepository;
import org.springframework.stereotype.Repository;
/**
 * 模板变量 Repository 接口
 * @author liaotao
 * @since 2019-01-22
 */
@Repository
public interface BaseTemplateVarRepository extends BaseRepository<BaseTemplateVar,String> {

}