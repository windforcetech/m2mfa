package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工单主档 服务类
 * @author liaotao
 * @since 2018-11-30
 */
public interface MesMoDescService extends BaseService<MesMoDesc,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoDesc> list(Query query);
}