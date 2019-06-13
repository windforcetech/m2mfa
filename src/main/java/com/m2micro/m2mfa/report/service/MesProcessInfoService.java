package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.entity.MesProcessInfo;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工单工序信息 服务类
 * @author chenshuhong
 * @since 2019-06-12
 */
public interface MesProcessInfoService extends BaseService<MesProcessInfo,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesProcessInfo> list(Query query);
}