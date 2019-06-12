package com.m2micro.m2mfa.record.service;

import com.m2micro.m2mfa.record.entity.MesRecordWipLog;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;

import java.util.Date;
import java.util.List;

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

    /**
     * 获取条码关联的排产单当前工序的产出
     * @param scheduleId
     * @param processId
     * @return
     */
    Integer getAllOutputQty(String scheduleId,String processId);

    /**
     * 过站的员工实时数据
     * @param scheduleId
     * @param processId
     * @param satffid
     * @param outTime
     * @return
     */
    Integer getActualOutput(String scheduleId, String processId, String satffid, Date outTime);


    /**
     * 获取条码关联的所有排产单当前工序的产出
     * @param scheduleIds
     * @param processId
     * @return
     */
    @Deprecated
    Integer getAllOutputQty(List<String> scheduleIds, String processId);
}
