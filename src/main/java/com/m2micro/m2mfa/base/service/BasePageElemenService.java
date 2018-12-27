package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 页面元素 服务类
 * @author chenshuhong
 * @since 2018-12-14
 */
public interface BasePageElemenService extends BaseService<BasePageElemen,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BasePageElemen> list(Query query);
}