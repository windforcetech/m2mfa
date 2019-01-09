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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesMoScheduleDispatchService mesMoScheduleDispatchService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    MesMoScheduleShiftRepository mesMoScheduleShiftRepository;


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
        return jdbcTemplate.query(sql, rowMapper);
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
                //获取所有的排产单相关数据
                ScheduleAllInfoModel scheduleAllInfoModel = mesMoScheduleService.getScheduleAllInfoModel(mesMoSchedule.getScheduleId());
                //填充所有数据
                fillScheduleAllInfo(scheduleAllInfoModel,mesMoSchedule,scheduleMachineParaModel);
                //保存所有的排产单相关数据
                saveAll(scheduleAllInfoModel);
            }
            //如果排产单状态是初始或是已审核
            else if(MoScheduleStatus.INITIAL.getKey().equals(mesMoSchedule.getFlag())||
                    MoScheduleStatus.AUDITED.getKey().equals(mesMoSchedule.getFlag())){
                //直接更新机台id
                mesMoScheduleRepository.updateMachineIdByScheduleId(scheduleMachineParaModel.getNewMachineId(),mesMoSchedule.getScheduleId());
            }
            else {
                //选中之后，排产单状态已发生改变，抛出异常
                throw new MMException("用户排产单【"+mesMoSchedule.getScheduleNo()+"】当前状态【"+MoScheduleStatus.valueOf(mesMoSchedule.getFlag()).getValue()+"】,不允许变更机台！");
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
        /*
            复制与此排产单相关的记录。Mes_Mo_Schedule_Shift，Mes_Mo_Schedule_Staff，Mes_Mo_Schedule_Process，
            Mes_Mo_Schedule_Station，自动在选择的机台重新生成新的排产记录。排产数量是上机台未完成数。状态为初始。
            PS：复制时有些内容需要根据当前的机台变更，如排产单号的生成。
        */
        //1.解析填充排产单信息
        MesMoSchedule newMesMoSchedule = scheduleAllInfoModel.getMesMoSchedule();
        newMesMoSchedule.setScheduleId(UUIDUtil.getUUID());
        newMesMoSchedule.setScheduleNo(mesMoScheduleService.getScheduleNoByMoId(mesMoSchedule.getMoId()));
        newMesMoSchedule.setMachineId(scheduleMachineParaModel.getNewMachineId());
        newMesMoSchedule.setScheduleQty(mesMoScheduleService.getUncompletedQty(mesMoSchedule.getScheduleId()));
        newMesMoSchedule.setCreateBy(null);
        newMesMoSchedule.setCreateOn(null);
        newMesMoSchedule.setModifiedBy(null);
        newMesMoSchedule.setCreateOn(null);
        //2.解析填充生产排程班别
        List<MesMoScheduleShift> mesMoScheduleShifts = scheduleAllInfoModel.getMesMoScheduleShifts();
        if(mesMoScheduleShifts!=null&&mesMoScheduleShifts.size()>0){
            mesMoScheduleShifts.stream().forEach(mesMoScheduleShift -> {
                mesMoScheduleShift.setId(UUIDUtil.getUUID());
                mesMoScheduleShift.setScheduleId(newMesMoSchedule.getScheduleId());
            });
        }
        //3.解析填充生产排程人员
        List<MesMoScheduleStaff> mesMoScheduleStaffs = scheduleAllInfoModel.getMesMoScheduleStaffs();
        if(mesMoScheduleStaffs!=null&&mesMoScheduleStaffs.size()>0){
            mesMoScheduleStaffs.stream().forEach(mesMoScheduleStaff -> {
                mesMoScheduleStaff.setId(UUIDUtil.getUUID());
                mesMoScheduleStaff.setScheduleId(newMesMoSchedule.getScheduleId());
                mesMoScheduleStaff.setActualStartTime(null);
                mesMoScheduleStaff.setActualEndTime(null);
                mesMoScheduleStaff.setCreateBy(null);
                mesMoScheduleStaff.setCreateOn(null);
                mesMoScheduleStaff.setModifiedBy(null);
                mesMoScheduleStaff.setModifiedOn(null);
            });
        }
        //4.解析填充生产排程工序
        List<MesMoScheduleProcess> mesMoScheduleProcesss = scheduleAllInfoModel.getMesMoScheduleProcesss();
        if(mesMoScheduleProcesss!=null&&mesMoScheduleProcesss.size()>0){
            mesMoScheduleProcesss.stream().forEach(mesMoScheduleProcess -> {
                mesMoScheduleProcess.setId(UUIDUtil.getUUID());
                mesMoScheduleProcess.setScheduleId(newMesMoSchedule.getScheduleId());
                mesMoScheduleProcess.setActualEndTime(null);
                mesMoScheduleProcess.setActualStartTime(null);
                mesMoScheduleProcess.setOutputQty(null);
                mesMoScheduleProcess.setCreateBy(null);
                mesMoScheduleProcess.setCreateOn(null);
                mesMoScheduleProcess.setModifiedBy(null);
                mesMoScheduleProcess.setModifiedOn(null);
            });
        }
        //5.解析填充生产排程工位
        List<MesMoScheduleStation> mesMoScheduleStations = scheduleAllInfoModel.getMesMoScheduleStations();
        if(mesMoScheduleStations!=null&&mesMoScheduleStations.size()>0){
            mesMoScheduleStations.stream().forEach(mesMoScheduleStation -> {
                mesMoScheduleStation.setId(UUIDUtil.getUUID());
                mesMoScheduleStation.setScheduleId(newMesMoSchedule.getScheduleId());
                mesMoScheduleStation.setCreateBy(null);
                mesMoScheduleStation.setCreateOn(null);
                mesMoScheduleStation.setModifiedBy(null);
                mesMoScheduleStation.setModifiedOn(null);
            });
        }
        return scheduleAllInfoModel;
    }

    private void saveAll(ScheduleAllInfoModel scheduleAllInfoModel){

    }
}
