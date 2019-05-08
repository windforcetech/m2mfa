package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordWipFail;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 在制不良记录表 服务类
 * @author chenshuhong
 * @since 2019-05-06
 */
public interface MesRecordWipFailService extends BaseService<MesRecordWipFail,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordWipFail> list(Query query);
}