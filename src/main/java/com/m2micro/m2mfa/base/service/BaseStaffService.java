package com.m2micro.m2mfa.base.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.query.BaseStaffQuery;
import com.m2micro.m2mfa.base.vo.BaseStaffDetailObj;

import java.util.List;

/**
 * 员工（职员）表 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BaseStaffService extends BaseService<BaseStaff,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */

    PageUtil<BaseStaffDetailObj> list(BaseStaffQuery query);

    List<BaseStaff> findByCodeAndStaffIdNot(String code, String staffId);

   // List<BaseStaff> findByCodeOrStaffNameOrdOrDutyIdIn(String code,String staffName,List<String> dutyIds);

}