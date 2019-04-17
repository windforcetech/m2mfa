package com.m2micro.m2mfa.barcode.service;

import com.m2micro.m2mfa.barcode.entity.BarcodePrintResources;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 标签打印记录 服务类
 * @author liaotao
 * @since 2019-03-27
 */
public interface BarcodePrintResourcesService extends BaseService<BarcodePrintResources,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<BarcodePrintResources> list(Query query);
}