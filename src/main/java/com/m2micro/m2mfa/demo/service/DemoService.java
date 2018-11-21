package com.m2micro.m2mfa.demo.service;

import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.demo.entity.Demo;

/**
 * @Auther: liaotao
 * @Date: 2018/11/19 10:03
 * @Description:
 */
public interface DemoService extends BaseService<Demo,Long> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<Demo> list(Query query);


}
