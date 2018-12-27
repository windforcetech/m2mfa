package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 人员作业记录 服务类
 * @author wanglei
 * @since 2018-12-27
 */
public interface MesRecordStaffService extends BaseService<MesRecordStaff,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordStaff> list(Query query);
}