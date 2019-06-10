package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.entity.MesStaffInfo;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 *  服务类
 * @author chenshuhong
 * @since 2019-06-10
 */
public interface MesStaffInfoService extends BaseService<MesStaffInfo,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesStaffInfo> list(Query query);
}