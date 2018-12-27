package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BasePack;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.query.BasePackQuery;

import java.util.List;

/**
 * 包装 服务类
 * @author wanglei
 * @since 2018-12-27
 */
public interface BasePackService extends BaseService<BasePack,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BasePack> list(BasePackQuery query);
    int countByPartIdAndCategory(String partId,Integer category);
    int countByIdNotAndPartIdAndCategory(String id,String partId,Integer category);
    List<BasePack> findByPartId(String partId);
    List<String> findByPartIdIn(List<String> partIds);
}