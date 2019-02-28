package com.m2micro.m2mfa.mo.service;

import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.framework.starter.entity.Organization;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.framework.commons.BaseService;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.mo.model.MesMoScheduleInfoModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.model.ScheduleAllInfoModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleQuery;
import com.m2micro.m2mfa.mo.vo.*;
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
     * 通过料件获取图称信息
     * @param partID
     * @return
     */
    List<MesMoDesc> findpartID(String partID);

    /**
     * 根据工单id排产单状态获取排产单信息
     * @param moId
     * @param flags
     * @return
     */
    List<MesMoSchedule> findByMoIdAndFlag(String moId,List<Integer> flags);

    /**
     * 审核排产单
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
     * 保存排产单相关的所有信息
     * @param scheduleAllInfoModel
     *          排产单所有相关的信息
     */
    void saveScheduleAllInfoModel(ScheduleAllInfoModel scheduleAllInfoModel);


    /**
     *排产单人员分配
     * @param mesMoScheduleStaffs
     * @param mesMoScheduleStations
     */
    void peopleDistributionsave( List<MesMoScheduleStaff> mesMoScheduleStaffs,List<MesMoScheduleStation> mesMoScheduleStations);

    /**
     * 判断排产单为生产中或已审待产
     * @param scheduleId
     * @return
     */
    boolean  isScheduleFlag(String  scheduleId);

}
