package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseBomDef;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 物料清单表明细 服务类
 *
 * @author liaotao
 * @since 2019-01-04
 */
public interface BaseBomDefService extends BaseService<BaseBomDef, String> {

     void deleteByBomIds(List<String> bomIds);
     List<BaseBomDef> findAllByBomId(String bomid);
}