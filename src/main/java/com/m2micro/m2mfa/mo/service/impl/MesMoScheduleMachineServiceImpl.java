package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.base.node.TreeNode;
import com.m2micro.m2mfa.common.util.PropertyUtil;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.common.util.ValidatorUtil;
import com.m2micro.m2mfa.common.validator.QueryGroup;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.*;
import com.m2micro.m2mfa.mo.model.MesMoScheduleMachineModel;
import com.m2micro.m2mfa.mo.model.MesMoScheduleModel;
import com.m2micro.m2mfa.mo.model.ScheduleAllInfoModel;
import com.m2micro.m2mfa.mo.model.ScheduleMachineParaModel;
import com.m2micro.m2mfa.mo.query.MesMoScheduleMachineQuery;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleShiftRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleDispatchService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleMachineService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.service.PadBottomDisplayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 11:32
 * @Description:    排产机台服务
 */
@Service
public class MesMoScheduleMachineServiceImpl implements MesMoScheduleMachineService {

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesMoScheduleDispatchService mesMoScheduleDispatchService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoScheduleShiftRepository mesMoScheduleShiftRepository;
    @Autowired
    PadBottomDisplayService padBottomDisplayService;


    @Override
    public TreeNode getAllDepartAndMachine() {
        return mesMoScheduleDispatchService.getAllDepartAndMachine();
    }

    @Override
    public List<MesMoScheduleModel> getScheduleForScheduleMachine(String machineId) {
        String sql= "SELECT\n" +
                    "	mms.schedule_id scheduleId,\n" +
                    "	mms.schedule_no scheduleNo,\n" +
                    "	mms.flag flag,\n" +
                    "	mms.enabled enabled,\n" +
                    "	bp.part_no partNo,\n" +
                    "	bp.name partName,\n" +
                    "	bm.name machineName,\n" +
                    "	IFNULL(mms.schedule_qty, 0) scheduleQty,\n" +
                    "	IFNULL(msp.output_qty, 0) outputQty\n" +
                    "FROM\n" +
                    "	mes_mo_schedule mms\n" +
                    "LEFT JOIN base_parts bp ON mms.part_id = bp.part_id\n" +
                    "LEFT JOIN base_machine bm ON mms.machine_id = bm.machine_id\n" +
                    "LEFT JOIN mes_part_route mpr ON mms.part_id = mpr.part_id\n" +
                    "LEFT JOIN mes_mo_schedule_process msp ON (\n" +
                    "	msp.process_id = mpr.output_process_id\n" +
                    "	AND msp.schedule_id = mms.schedule_id\n" +
                    ")\n" +
                    "WHERE\n" +
                    "	1 = 1\n" +
                    "AND (mms.flag = "+ MoScheduleStatus.INITIAL.getKey() +
                    " OR mms.flag = "+ MoScheduleStatus.AUDITED.getKey() +
                    " OR mms.flag = "+ MoScheduleStatus.PRODUCTION.getKey() + ")\n";
        if(StringUtils.isNotEmpty(machineId)){
            sql = sql + " AND mms.machine_id = '" + machineId + "'\n";
        }
        sql = sql + " ORDER BY mms.sequence ASC";
        RowMapper<MesMoScheduleModel> rowMapper = BeanPropertyRowMapper.newInstance(MesMoScheduleModel.class);
        List<MesMoScheduleModel> list = jdbcTemplate.query(sql, rowMapper);
        return mesMoScheduleService.getScheduleModelForOutput(list);
    }

    private Integer getOutput(String scheduleId) {
        List scheduleIds = new ArrayList();
        scheduleIds.add(scheduleId);
        return padBottomDisplayService.getOutPutQtys(scheduleId, scheduleIds);
    }

    @Override
    public PageUtil<MesMoScheduleMachineModel> list(MesMoScheduleMachineQuery query) {
        String sql =  "SELECT\n" +
                    "	bm.machine_id machineId,\n" +
                    "	bm.code code,\n" +
                    "	bm.name name,\n" +
                    "	o.department_name departmentName,\n" +
                    "	bi.item_name flagName\n" +
                    "FROM\n" +
                    "	base_machine bm\n" +
                    "LEFT JOIN base_items_target bi ON bm.flag = bi.id\n" +
                    "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                    "WHERE\n" +
                    "	(\n" +
                    "		bi.item_value = 0\n" +
                    "		OR bi.item_value = 1\n" +
                    "		OR bi.item_value = 4\n" +
                    "	)\n";

        if(StringUtils.isNotEmpty(query.getCode())){
            sql = sql + " and bm.code like '%"+query.getCode()+"%'";
        }
        if(StringUtils.isNotEmpty(query.getMachineId())){
            sql = sql + " and bm.machine_id <> '"+query.getMachineId()+"'";
        }
        sql = sql + " order by bm.modified_on desc";
        sql = sql + " limit "+(query.getPage()-1)*query.getSize()+","+query.getSize();
        RowMapper rm = BeanPropertyRowMapper.newInstance(MesMoScheduleMachineModel.class);
        List<MesMoScheduleMachineModel> list = jdbcTemplate.query(sql,rm);

        String countSql = "SELECT\n" +
                        "	count(*)\n" +
                        "FROM\n" +
                        "	base_machine bm\n" +
                        "LEFT JOIN base_items_target bi ON bm.flag = bi.id\n" +
                        "LEFT JOIN organization o ON bm.department_id = o.uuid\n" +
                        "WHERE\n" +
                        "	(\n" +
                        "		bi.item_value = 0\n" +
                        "		OR bi.item_value = 1\n" +
                        "		OR bi.item_value = 4\n" +
                        "	)\n" ;
        if(StringUtils.isNotEmpty(query.getMachineId())){
            countSql = countSql + " and bm.machine_id <> '"+query.getMachineId()+"'";
        }
        long totalCount = jdbcTemplate.queryForObject(countSql,long.class);
        return PageUtil.of(list,totalCount,query.getSize(),query.getPage());
    }

    @Override
    @Transactional
    public void change(ScheduleMachineParaModel scheduleMachineParaModel) {
        ValidatorUtil.validateEntity(scheduleMachineParaModel, QueryGroup.class);
        //获取选中的所有排产单
        List<MesMoSchedule> mesMoSchedules = mesMoScheduleRepository.findByScheduleIdIn(scheduleMachineParaModel.getScheduleIds());
        //遍历排产单
        for (MesMoSchedule mesMoSchedule:mesMoSchedules){
            //如果排产单状态是生产中
            if(MoScheduleStatus.PRODUCTION.getKey().equals(mesMoSchedule.getFlag())){

                /*
                    复制与此排产单相关的记录。Mes_Mo_Schedule_Shift，Mes_Mo_Schedule_Staff，Mes_Mo_Schedule_Process，
                    Mes_Mo_Schedule_Station，自动在选择的机台重新生成新的排产记录。排产数量是上机台未完成数。状态为初始。
                    PS：复制时有些内容需要根据当前的机台变更，如排产单号的生成。
                */
                //获取所有的排产单相关数据
                ScheduleAllInfoModel scheduleAllInfoModel = mesMoScheduleService.getScheduleAllInfoModel(mesMoSchedule.getScheduleId());
                //填充所有数据
                ScheduleAllInfoModel newScheduleAllInfoModel = fillScheduleAllInfo(scheduleAllInfoModel,mesMoSchedule,scheduleMachineParaModel);
                //保存所有的排产单相关数据
                saveAll(newScheduleAllInfoModel);
                //原有排产单结案
                MesMoSchedule mesMoSchedule1 = scheduleAllInfoModel.getMesMoSchedule();
                mesMoSchedule1.setFlag(MoScheduleStatus.FORCECLOSE.getKey());
                mesMoScheduleService.save(mesMoSchedule1);
            }
            //如果排产单状态是初始或是已审核
            else if(MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag())||
                    MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())){
                //新机台上的最大顺序
                Integer integer = mesMoScheduleService.maxSequence(scheduleMachineParaModel.getNewMachineId());
                //直接更新机台id
                mesMoScheduleRepository.updateMachineIdByScheduleId(scheduleMachineParaModel.getNewMachineId(),integer+1,mesMoSchedule.getScheduleId());
            }
            else {
                //选中之后，排产单状态已发生改变，抛出异常
                throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许变更机台,请刷新页面！");
            }

        }
    }

    /**
     * 获取所有的排产单相关数据（解析，填充）
     * @param scheduleAllInfoModel
     * @param scheduleMachineParaModel
     * @return
     */
    private ScheduleAllInfoModel fillScheduleAllInfo(ScheduleAllInfoModel scheduleAllInfoModel, MesMoSchedule mesMoSchedule,ScheduleMachineParaModel scheduleMachineParaModel){
        ScheduleAllInfoModel newScheduleAllInfoModel = new ScheduleAllInfoModel();
        //1.解析填充排产单信息
        setMesMoSchedule(scheduleAllInfoModel, mesMoSchedule, scheduleMachineParaModel, newScheduleAllInfoModel);
        //2.解析填充生产排程班别
        setMesMoScheduleShifts(scheduleAllInfoModel, newScheduleAllInfoModel, newScheduleAllInfoModel.getMesMoSchedule());
        //3.解析填充生产排程人员
        setMesMoScheduleStaffs(scheduleAllInfoModel, newScheduleAllInfoModel, newScheduleAllInfoModel.getMesMoSchedule());
        //4.解析填充生产排程工序
        setMesMoScheduleProcesss(scheduleAllInfoModel, newScheduleAllInfoModel, newScheduleAllInfoModel.getMesMoSchedule());
        //5.解析填充生产排程工位
        setMesMoScheduleStations(scheduleAllInfoModel, newScheduleAllInfoModel, newScheduleAllInfoModel.getMesMoSchedule());
        return newScheduleAllInfoModel;
    }

    /**
     * 解析填充生产排程工位
     * @param scheduleAllInfoModel
     * @param newScheduleAllInfoModel
     * @param newMesMoSchedule
     */
    private void setMesMoScheduleStations(ScheduleAllInfoModel scheduleAllInfoModel, ScheduleAllInfoModel newScheduleAllInfoModel, MesMoSchedule newMesMoSchedule) {
        List<MesMoScheduleStation> mesMoScheduleStations = scheduleAllInfoModel.getMesMoScheduleStations();
        List<MesMoScheduleStation> newMesMoScheduleStations = new ArrayList<>();
        if(mesMoScheduleStations!=null&&mesMoScheduleStations.size()>0){
            mesMoScheduleStations.stream().forEach(mesMoScheduleStation -> {
                MesMoScheduleStation newMesMoScheduleStation = new MesMoScheduleStation();
                PropertyUtil.copyToNew(newMesMoScheduleStation,mesMoScheduleStation);
                newMesMoScheduleStation.setId(UUIDUtil.getUUID());
                newMesMoScheduleStation.setScheduleId(newMesMoSchedule.getScheduleId());
                newMesMoScheduleStation.setCreateBy(null);
                newMesMoScheduleStation.setCreateOn(null);
                newMesMoScheduleStation.setModifiedBy(null);
                newMesMoScheduleStation.setModifiedOn(null);
                newMesMoScheduleStations.add(newMesMoScheduleStation);
            });
        }
        newScheduleAllInfoModel.setMesMoScheduleStations(newMesMoScheduleStations);
    }

    /**
     * 解析填充生产排程工序
     * @param scheduleAllInfoModel
     * @param newScheduleAllInfoModel
     * @param newMesMoSchedule
     */
    private void setMesMoScheduleProcesss(ScheduleAllInfoModel scheduleAllInfoModel, ScheduleAllInfoModel newScheduleAllInfoModel, MesMoSchedule newMesMoSchedule) {
        List<MesMoScheduleProcess> mesMoScheduleProcesss = scheduleAllInfoModel.getMesMoScheduleProcesss();
        List<MesMoScheduleProcess> newMesMoScheduleProcesss = new ArrayList<>();
        if(mesMoScheduleProcesss!=null&&mesMoScheduleProcesss.size()>0){
            mesMoScheduleProcesss.stream().forEach(mesMoScheduleProcess -> {
                MesMoScheduleProcess newMesMoScheduleProcess = new MesMoScheduleProcess();
                PropertyUtil.copyToNew(newMesMoScheduleProcess,mesMoScheduleProcess);
                newMesMoScheduleProcess.setId(UUIDUtil.getUUID());
                newMesMoScheduleProcess.setScheduleId(newMesMoSchedule.getScheduleId());
                newMesMoScheduleProcess.setActualEndTime(null);
                newMesMoScheduleProcess.setActualStartTime(null);
                newMesMoScheduleProcess.setOutputQty(null);
                newMesMoScheduleProcess.setCreateBy(null);
                newMesMoScheduleProcess.setCreateOn(null);
                newMesMoScheduleProcess.setModifiedBy(null);
                newMesMoScheduleProcess.setModifiedOn(null);
                newMesMoScheduleProcesss.add(newMesMoScheduleProcess);
            });
        }
        newScheduleAllInfoModel.setMesMoScheduleProcesss(newMesMoScheduleProcesss);
    }

    /**
     * 解析填充生产排程人员
     * @param scheduleAllInfoModel
     * @param newScheduleAllInfoModel
     * @param newMesMoSchedule
     */
    private void setMesMoScheduleStaffs(ScheduleAllInfoModel scheduleAllInfoModel, ScheduleAllInfoModel newScheduleAllInfoModel, MesMoSchedule newMesMoSchedule) {
        List<MesMoScheduleStaff> mesMoScheduleStaffs = scheduleAllInfoModel.getMesMoScheduleStaffs();
        List<MesMoScheduleStaff> newMesMoScheduleStaffs = new ArrayList<>();
        if(mesMoScheduleStaffs!=null&&mesMoScheduleStaffs.size()>0){
            mesMoScheduleStaffs.stream().forEach(mesMoScheduleStaff -> {
                MesMoScheduleStaff newMesMoScheduleStaff = new MesMoScheduleStaff();
                PropertyUtil.copyToNew(newMesMoScheduleStaff,mesMoScheduleStaff);
                newMesMoScheduleStaff.setId(UUIDUtil.getUUID());
                newMesMoScheduleStaff.setScheduleId(newMesMoSchedule.getScheduleId());
                newMesMoScheduleStaff.setActualStartTime(null);
                newMesMoScheduleStaff.setActualEndTime(null);
                newMesMoScheduleStaff.setCreateBy(null);
                newMesMoScheduleStaff.setCreateOn(null);
                newMesMoScheduleStaff.setModifiedBy(null);
                newMesMoScheduleStaff.setModifiedOn(null);
                newMesMoScheduleStaffs.add(newMesMoScheduleStaff);
            });
        }
        newScheduleAllInfoModel.setMesMoScheduleStaffs(newMesMoScheduleStaffs);
    }

    /**
     * 解析填充生产排程班别
     * @param scheduleAllInfoModel
     * @param newScheduleAllInfoModel
     * @param newMesMoSchedule
     */
    private void setMesMoScheduleShifts(ScheduleAllInfoModel scheduleAllInfoModel, ScheduleAllInfoModel newScheduleAllInfoModel, MesMoSchedule newMesMoSchedule) {
        List<MesMoScheduleShift> mesMoScheduleShifts = scheduleAllInfoModel.getMesMoScheduleShifts();
        List<MesMoScheduleShift> newMesMoScheduleShifts = new ArrayList<>();
        if(mesMoScheduleShifts!=null&&mesMoScheduleShifts.size()>0){
            mesMoScheduleShifts.stream().forEach(mesMoScheduleShift -> {
                MesMoScheduleShift newMesMoScheduleShift = new MesMoScheduleShift();
                PropertyUtil.copyToNew(newMesMoScheduleShift,mesMoScheduleShift);
                newMesMoScheduleShift.setId(UUIDUtil.getUUID());
                newMesMoScheduleShift.setScheduleId(newMesMoSchedule.getScheduleId());
                newMesMoScheduleShift.setCreateBy(null);
                newMesMoScheduleShift.setCreateOn(null);
                newMesMoScheduleShift.setModifiedBy(null);
                newMesMoScheduleShift.setModifiedOn(null);
                newMesMoScheduleShifts.add(newMesMoScheduleShift);
            });
        }
        newScheduleAllInfoModel.setMesMoScheduleShifts(newMesMoScheduleShifts);
    }

    /**
     * 解析填充排产单信息
     * @param scheduleAllInfoModel
     * @param mesMoSchedule
     * @param scheduleMachineParaModel
     * @param newScheduleAllInfoModel
     * @return
     */
    private MesMoSchedule setMesMoSchedule(ScheduleAllInfoModel scheduleAllInfoModel, MesMoSchedule mesMoSchedule, ScheduleMachineParaModel scheduleMachineParaModel, ScheduleAllInfoModel newScheduleAllInfoModel) {
        MesMoSchedule oldMesMoSchedule = scheduleAllInfoModel.getMesMoSchedule();
        MesMoSchedule newMesMoSchedule = new MesMoSchedule();
        PropertyUtil.copyToNew(newMesMoSchedule,oldMesMoSchedule);
        newMesMoSchedule.setScheduleId(UUIDUtil.getUUID());
        newMesMoSchedule.setFlag(MoScheduleStatus.INITIAL.getKey());
        newMesMoSchedule.setScheduleNo(mesMoScheduleService.getScheduleNoByMoId(mesMoSchedule.getMoId()));
        newMesMoSchedule.setMachineId(scheduleMachineParaModel.getNewMachineId());

        Integer outPutQtys = getOutput(mesMoSchedule.getScheduleId());
        newMesMoSchedule.setScheduleQty(mesMoSchedule.getScheduleQty()-outPutQtys);

        newMesMoSchedule.setCreateBy(null);
        newMesMoSchedule.setCreateOn(null);
        newMesMoSchedule.setModifiedBy(null);
        newMesMoSchedule.setModifiedOn(null);
        newScheduleAllInfoModel.setMesMoSchedule(newMesMoSchedule);
        return newMesMoSchedule;
    }

    /**
     * 保存所有
     * @param scheduleAllInfoModel
     */
    @Transactional
    public void saveAll(ScheduleAllInfoModel scheduleAllInfoModel){
        mesMoScheduleService.saveScheduleAllInfoModel(scheduleAllInfoModel);
    }
}
