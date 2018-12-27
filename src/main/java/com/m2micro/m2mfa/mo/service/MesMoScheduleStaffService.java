package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 生产排程人员 服务类
 * @author liaotao
 * @since 2018-12-26
 */
public interface MesMoScheduleStaffService extends BaseService<MesMoScheduleStaff,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoScheduleStaff> list(Query query);
}