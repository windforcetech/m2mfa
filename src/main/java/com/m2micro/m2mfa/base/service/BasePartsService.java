package com.m2micro.m2mfa.base.service;

import com.m2micro.m2mfa.base.entity.BaseParts;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 料件基本资料 服务类
 * @author liaotao
 * @since 2018-11-26
 */
public interface BasePartsService extends BaseService<BaseParts,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BaseParts> list(Query query);
}