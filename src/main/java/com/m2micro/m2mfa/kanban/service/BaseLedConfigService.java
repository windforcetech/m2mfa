package com.m2micro.m2mfa.kanban.service;

import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 *  服务类
 * @author chenshuhong
 * @since 2019-05-27
 */
public interface BaseLedConfigService extends BaseService<BaseLedConfig,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseLedConfig> list(Query query);
}