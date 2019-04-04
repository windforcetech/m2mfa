package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.m2mfa.base.entity.BaseBomDef;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import com.m2micro.m2mfa.base.entity.BaseBomSubstitute;

import java.util.List;

/**
 * 物料清单表 服务类
 *
 * @author liaotao
 * @since 2019-01-04
 */
public interface BaseBomSubstituteService extends BaseService<BaseBomSubstitute, String> {

    List<BaseBomSubstitute> findAllByBomId(String bomid);

}