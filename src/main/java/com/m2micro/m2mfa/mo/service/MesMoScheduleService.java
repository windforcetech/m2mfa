package com.m2micro.m2mfa.mo.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.model.OperationInfo;

import java.util.List;

/**
 * 生产排程表表头 服务类
 * @author liaotao
 * @since 2018-12-26
 */
public interface MesMoScheduleService extends BaseService<MesMoSchedule,String> {
    /**
     * 分页查询
     * @param query
     *         查询参数
     * @return  分页信息
     */
    PageUtil<MesMoSchedule> list(Query query);

    /**
     * 获取当前用户下的排产单
     * @param staffId
     *          员工工号id
     * @return
     */
    MesMoSchedule getMesMoScheduleByStaffId(String staffId);

    /**
     * 获取待处理的工位
     * @param staffId
     *          员工工号id
     * @param scheduleId
     *          排产单id
     * @return
     */
    List<BaseStation> getPendingStations(String staffId, String scheduleId);

    /**
     * 获取操作栏相关信息
     * @param staffId
     * @param scheduleId
     * @param stationId
     */
    OperationInfo getOperationInfo(String staffId, String scheduleId, String stationId);

    List<MesMoSchedule> findpartID(String partID);

    /**
     * 根据工单id排产单状态获取排产单信息
     * @param moId
     * @param flags
     * @return
     */
    List<MesMoSchedule> findByMoIdAndFlag(String moId,List<Integer> flags);

    /**
     * 审核工单
     * @param id
     *      工单id
     */
    void auditing(String id);

    /**
     * 取消审核工单
     * @param id
     *      工单id
     */
    void cancel(String id);

    /**
     * 冻结工单
     * @param id
     *      工单id
     */
    void frozen(String id);

    /**
     * 解冻工单
     * @param id
     *      工单id
     */
    void unfreeze(String id);

    /**
     * 强制结案
     * @param id
     *      工单id
     */
    void forceClose(String id);


}