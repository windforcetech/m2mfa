package com.m2micro.m2mfa.report.service;

import com.m2micro.m2mfa.report.entity.MesInfo;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工单信息 服务类
 * @author liaotao
 * @since 2019-06-12
 */
public interface MesInfoService extends BaseService<MesInfo,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesInfo> list(Query query);
}