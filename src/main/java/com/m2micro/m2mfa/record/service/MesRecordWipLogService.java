package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 在制记录表历史 服务类
 * @author liaotao
 * @since 2019-03-27
 */
public interface MesRecordWipLogService extends BaseService<MesRecordWipLog,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordWipLog> list(Query query);
}