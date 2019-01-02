package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordLotparts;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 加料记录表 服务类
 * @author liaotao
 * @since 2019-01-02
 */
public interface MesRecordLotpartsService extends BaseService<MesRecordLotparts,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordLotparts> list(Query query);
}