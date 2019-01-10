package com.m2micro.m2mfa.mo.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import com.m2micro.m2mfa.mo.model.MesMoScheduleInfoModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.model.ScheduleAllInfoModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.vo.PeopleDistribution;
import com.m2micro.m2mfa.mo.vo.ProcessStatus;
import com.m2micro.m2mfa.mo.vo.ProductionProcess;
import com.m2micro.m2mfa.mo.vo.Productionorder;
import com.m2micro.m2mfa.pr.vo.MesPartvo;

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
    PageUtil<MesMoScheduleModel> list(MesMoScheduleQuery query);

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

    /**
     * 通过料件获取图称信息
     * @param partID
     * @return
     */
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

    /**
     * 根据工单获取图称
     * @param moId
     * @return
     */
    MesPartvo findbyMoId(String moId  );

    /**
     * 排产单添加
     * @param mesMoSchedule
     * @param mesMoScheduleStaffs
     * @param mesMoScheduleProcesses
     * @param mesMoScheduleStations
     */
    void save(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations);

    /**
     * 排产单更新
     * @param mesMoSchedule
     * @param mesMoScheduleStaffs
     * @param mesMoScheduleProcesses
     * @param mesMoScheduleStations
     */
    void updateMesMoSchedule(MesMoSchedule mesMoSchedule, List<MesMoScheduleStaff> mesMoScheduleStaffs, List<MesMoScheduleProcess> mesMoScheduleProcesses, List<MesMoScheduleStation> mesMoScheduleStations);
    /**
     * 排产单新增要返回的数据
     * @return  排产单新增要返回的数据
     */
    MesMoScheduleInfoModel addDetails();

    /**
     * 排产单获取
     * @param scheduleId
     * @return
     */
    ProductionProcess info(String scheduleId );

    /**
     * 获取所有岗位信息
     * @return
     */
    List<Organization>findbPosition();

    /**
     * 删除多个排产单
     * @param ids
     */
    String  deleteIds(String [] ids);

    /**
     * 获取排产单编号（自动生成）
     * @param moId
     * @return
     */
    String getScheduleNoByMoId(String moId);

    /**
     * 结束工序
     * @param processStatus
     */
    void processEnd(ProcessStatus processStatus);

    /**
     * 工序恢复
     * @param processStatus
     */
    void processRestore(ProcessStatus processStatus);

    /**
     * 获取未完成的排产单产量
     * @param scheduleId
     * @return
     */
    Integer getUncompletedQty(String scheduleId);

    /**
     * 获取排产单所有相关的信息
     * @param scheduleId
     *          排产单id
     * @return  排产单所有相关的信息
     */
    ScheduleAllInfoModel getScheduleAllInfoModel(String scheduleId);

    /**
     * 排产单人员安排显示
     * @return
     */
    List<PeopleDistribution> peopleDistribution();

    /**
     * 机台获取排产单
     * @param machineId
     * @return
     */
    List<MesMoSchedule>  findbMachineId(String machineId );
}