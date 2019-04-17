package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 工单料表 服务类
 * @author liaotao
 * @since 2019-03-29
 */
public interface MesMoBomService extends BaseService<MesMoBom,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoBom> list(Query query);
}