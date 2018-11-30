package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工位基本档 服务类
 * @author liaotao
 * @since 2018-11-30
 */
public interface BaseStationService extends BaseService<BaseStation,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseStation> list(Query query);
}