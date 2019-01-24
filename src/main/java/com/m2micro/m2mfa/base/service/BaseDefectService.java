package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 不良現象代碼 服务类
 * @author chenshuhong
 * @since 2019-01-24
 */
public interface BaseDefectService extends BaseService<BaseDefect,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseDefect> list(Query query);
}
