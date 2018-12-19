package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工序工位关系 服务类
 * @author chenshuhong
 * @since 2018-12-14
 */
public interface BaseProcessStationService extends BaseService<BaseProcessStation,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseProcessStation> list(Query query);
    //根据工序主键删除工序工位关系
    void deleteprocessId(String processId);
}