package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordMachine;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 机台抄读历史记录 服务类
 * @author chenshuhong
 * @since 2019-06-19
 */
public interface MesRecordMachineService extends BaseService<MesRecordMachine,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesRecordMachine> list(Query query);
}