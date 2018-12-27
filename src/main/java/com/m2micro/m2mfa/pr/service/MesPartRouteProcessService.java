package com.m2micro.m2mfa.pr.service;

import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 料件途程设定工序 服务类
 * @author liaotao
 * @since 2018-12-19
 */
public interface MesPartRouteProcessService extends BaseService<MesPartRouteProcess,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesPartRouteProcess> list(Query query);

    //途程id删除关联数据
    void deleteParRouteID(String parrouteid);
}