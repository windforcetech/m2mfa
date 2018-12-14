package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工序基本档 服务类
 * @author chenshuhong
 * @since 2018-12-14
 */
public interface BaseProcessService extends BaseService<BaseProcess,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseProcess> list(Query query);
}