package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseStaffshift;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 员工排班表 服务类
 * @author liaotao
 * @since 2019-01-04
 */
public interface BaseStaffshiftService extends BaseService<BaseStaffshift,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseStaffshift> list(Query query);
}