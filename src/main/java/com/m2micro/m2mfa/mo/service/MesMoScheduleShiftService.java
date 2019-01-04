package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleShift;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 *  服务类
 * @author liaotao
 * @since 2019-01-04
 */
public interface MesMoScheduleShiftService extends BaseService<MesMoScheduleShift,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoScheduleShift> list(Query query);
}