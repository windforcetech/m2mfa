package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
/**
 * 生产排程工序 服务类
 * @author liaotao
 * @since 2019-01-02
 */
public interface MesMoScheduleProcessService extends BaseService<MesMoScheduleProcess,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoScheduleProcess> list(Query query);

    /**
     * 判断当前排产单的工序是否结束
     * @param scheduleId
     * @param processId
     * @return
     */
    Boolean isEndProcess(String scheduleId,String processId);

    /**
     * 结束工序
     * @param scheduleId
     * @param processId
     */
    void endProcess(String scheduleId,String processId);

    /**
     * 更新工序的产量和模数
     * @param scheduleId
     *          排产单id
     * @param processId
     *          工序id
     * @param outputQty
     *          产量
     * @param mold
     *          模数
     */
    void updateOutputQtyAndMold(String scheduleId,String processId,Integer outputQty,Integer mold);

    /**
     * 更新工序的产量和模数
     * @param mesMoScheduleProcess
     *          生产排程工序
     * @param outputQty
     *          产量
     * @param mold
     *          模数
     */
    void updateOutputQtyAndMold(MesMoScheduleProcess mesMoScheduleProcess,Integer outputQty,Integer mold);

    /**
     * 更新工序的产量减去不良数
     * @param scheduleId
     *          排产单id
     * @param processId
     *          工序id
     * @param fail
     *          不良数
     */
    void updateOutputQtyForFail(String scheduleId,String processId,Integer fail);


    /**
     * 更新工序的产量加上产出数
     * @param scheduleId
     *          排产单id
     * @param processId
     *          工序id
     * @param outputQty
     *          产出数
     */
    void updateOutputQtyForAdd(String scheduleId,String processId,Integer outputQty);
}