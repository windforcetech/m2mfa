package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 上工记录表 服务类
 * @author liaotao
 * @since 2018-12-26
 */
public interface MesRecordWorkService extends BaseService<MesRecordWork,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordWork> list(Query query);
}