package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseBomDesc;
import com.m2micro.m2mfa.base.entity.BaseShift;
import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.m2mfa.base.query.BaseStaffshiftQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 物料清单表 服务类
 *
 * @author liaotao
 * @since 2019-01-04
 */
public interface BaseBomDescService extends BaseService<BaseBomDesc, String> {

    List<BaseBomDesc> findAllByPartId(String partid);

}