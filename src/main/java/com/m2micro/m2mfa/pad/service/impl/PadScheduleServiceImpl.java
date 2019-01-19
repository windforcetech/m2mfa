package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.InitData;
import com.m2micro.m2mfa.pad.model.PadScheduleModel;
import com.m2micro.m2mfa.pad.model.PadStationModel;
import com.m2micro.m2mfa.pad.model.StationAndOperate;
import com.m2micro.m2mfa.pad.service.PadDispatchService;
import com.m2micro.m2mfa.pad.service.PadScheduleService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import com.m2micro.m2mfa.record.entity.MesRecordWork;
import com.m2micro.m2mfa.record.repository.MesRecordWorkRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 15:03
 * @Description:
 */
@Service("padScheduleService")
public class PadScheduleServiceImpl implements PadScheduleService {
    @Autowired
    MesMoScheduleService mesMoScheduleService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    BaseStaffRepository baseStaffRepository;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesRecordWorkRepository mesRecordWorkRepository;
    @Autowired
    PadDispatchService padDispatchService;

    @Override
    public List<PadScheduleModel> getMesMoSchedule() {
        return getMesMoScheduleByBaseStaff(PadStaffUtil.getStaff());
    }


    @Override
    public List<PadScheduleModel> getMesMoScheduleByStaffNo(String staffNo) {
        BaseStaff baseStaff = baseStaffRepository.findByCode(staffNo);
        if(baseStaff==null){
            throw new MMException("不存在该员工");
        }
        //获取该员工下已审待产和生产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        return getMesMoScheduleByBaseStaff(baseStaff);
    }

    /**
     * 获取该员工下已审待产和生产中每个机器上的排产单（每个机器只取优先级最高的排产单）
     * @param baseStaff
     * @return
     */
    private List<PadScheduleModel> getMesMoScheduleByBaseStaff(BaseStaff baseStaff) {
        //获取生产中的排产单并且对该员工至少有一个工位没完成（排除掉所有分配给该员工的工位都已完成的排产单）
        List<PadScheduleModel> production = getProductionScheduleModels(baseStaff);
        if(production!=null&&production.size()>0){
            return production;
        }
        //获取已审待产的在同一机台上的优先级最高的排产单(过滤掉排产单不是当前机台优先级最高的排产单)
        return getFilterScheduleModels(baseStaff);
    }

    /**
     * 获取生产中的排产单并且对该员工至少有一个工位没完成（排除掉所有分配给该员工的工位都已完成的排产单）
     * @param baseStaff
     * @return
     */
    private List<PadScheduleModel> getProductionScheduleModels(BaseStaff baseStaff) {
        //获取该员工下生产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        String sqlProduction = "SELECT\n" +
                "	ms.schedule_id scheduleId,\n" +
                "	ms.schedule_no scheduleNo,\n" +
                "	min(ms.sequence) sequence,\n" +
                "	ms.machine_id machineId,\n" +
                "	IF(ms.flag="+ MoScheduleStatus.AUDITED.getKey() +",'"+MoScheduleStatus.AUDITED.getValue()+"','"+MoScheduleStatus.PRODUCTION.getValue()+"') flagStatus,\n" +
                "	bm.name machineName\n" +
                "FROM\n" +
                "	mes_mo_schedule ms,\n" +
                "	mes_mo_schedule_staff mss,\n" +
                "	base_machine bm\n" +
                "WHERE\n" +
                "	mss.schedule_id = ms.schedule_id \n" +
                "AND ms.flag = "+ MoScheduleStatus.PRODUCTION.getKey() +"\n" +
                "AND bm.machine_id = ms.machine_id\n" +
                "AND mss.staff_id = '"+ baseStaff.getStaffId() + "'\n" +
                "GROUP BY\n" +
                "	ms.machine_id\n" +
                "ORDER BY\n" +
                "	ms.sequence ASC,\n" +
                "	bm.code ASC";
        RowMapper<PadScheduleModel> rowMapperProduction = BeanPropertyRowMapper.newInstance(PadScheduleModel.class);
        return jdbcTemplate.query(sqlProduction, rowMapperProduction);
    }

    /**
     * 获取已审待产的在同一机台上的优先级最高的排产单（保证不插队）
     * @param baseStaff
     * @return
     */
    private List<PadScheduleModel> getFilterScheduleModels(BaseStaff baseStaff) {
        //获取该员工下已审待产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        List<PadScheduleModel> auditeds = getAuditedScheduleModels(baseStaff);
        //过滤后的排产单相关信息
        List<PadScheduleModel> filterPadScheduleModel = new ArrayList<>();
        if(auditeds!=null&&auditeds.size()>0){
            for (PadScheduleModel audited:auditeds) {
                //通过机台id找到当前机台的优先级最高的排产单
                MesMoSchedule mesMoSchedule = mesMoScheduleRepository.getFirstMesMoScheduleByMachineId(audited.getMachineId(), MoScheduleStatus.AUDITED.getKey());
                //如果该排产单就是当前机台优先级最高的排产单则可以生产，不然舍弃。（保证不插队）
                if(audited.getScheduleId().equals(mesMoSchedule.getScheduleId())){
                    filterPadScheduleModel.add(audited);
                }
            }
        }
        return filterPadScheduleModel;
    }

    /**
     * 获取该员工下已审待产中每个机器上的排产单（每个机器只取优先级最高的排产单）
     * @param baseStaff
     * @return
     */
    private List<PadScheduleModel> getAuditedScheduleModels(BaseStaff baseStaff) {
        //获取该员工下已审待产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        String sqlAudited = "SELECT\n" +
                "	ms.schedule_id scheduleId,\n" +
                "	ms.schedule_no scheduleNo,\n" +
                "	min(ms.sequence) sequence,\n" +
                "	ms.machine_id machineId,\n" +
                "	IF(ms.flag="+ MoScheduleStatus.AUDITED.getKey() +",'"+MoScheduleStatus.AUDITED.getValue()+"','"+MoScheduleStatus.PRODUCTION.getValue()+"') flagStatus,\n" +
                "	bm.name machineName\n" +
                "FROM\n" +
                "	mes_mo_schedule ms,\n" +
                "	mes_mo_schedule_staff mss,\n" +
                "	base_machine bm\n" +
                "WHERE\n" +
                "	mss.schedule_id = ms.schedule_id \n" +
                "AND ms.flag = "+ MoScheduleStatus.AUDITED.getKey() +"\n" +
                "AND bm.machine_id = ms.machine_id\n" +
                "AND mss.staff_id = '"+ baseStaff.getStaffId() + "'\n" +
                "GROUP BY\n" +
                "	ms.machine_id\n" +
                "ORDER BY\n" +
                "	ms.sequence ASC,\n" +
                "	bm.code ASC";
        RowMapper<PadScheduleModel> rowMapperAudited = BeanPropertyRowMapper.newInstance(PadScheduleModel.class);
        return jdbcTemplate.query(sqlAudited, rowMapperAudited);
    }



    @Override
    public List<PadStationModel> getPendingStations(String scheduleId) {
        BaseStaff baseStaff = PadStaffUtil.getStaff();
        if(baseStaff==null){
            throw new MMException("不存在该员工");
        }
        if(StringUtils.isEmpty(scheduleId)){
            throw new MMException("当前没有可处理的排产单！");
        }
        return getPadStationModels(scheduleId, baseStaff);
    }


    /**
     * 获取待处理的工位
     * @param scheduleId
     *          排产单id
     * @param baseStaff
     *          员工信息
     * @return  待处理的工位
     */
    private List<PadStationModel> getPadStationModels(String scheduleId, BaseStaff baseStaff) {
        //获取待处理的所有工位
        List<PadStationModel> baseStations = getAllBaseStations(baseStaff.getStaffId(), scheduleId);
        //获取待处理的所有已排除跳过的工位
        List<PadStationModel> excludedBaseStations = getAllExcludedBaseStations(baseStaff.getStaffId(), scheduleId);
        //是否返回跳过的工位标志
        Boolean flag = isJump(scheduleId);
        //返回跳过的所有工位
        if (!flag) return excludedBaseStations;
        //如果不相同返回所有工位
        return baseStations;
    }

    /**
     * 是否返回跳过的工位
     * @param scheduleId
     * @return
     */
    private Boolean isJump(String scheduleId) {
        //获取当前排产单
        MesMoSchedule current = mesMoScheduleRepository.findById(scheduleId).orElse(null);
        if (current == null) {
            throw new MMException("数据库不存在该排产单！");
        }
        //获取当前机台的上一次处理的排产单
        MesRecordWork oldMesRecordWork = mesRecordWorkRepository.getOldMesRecordWork(scheduleId, current.getMachineId());
        //为空说明该机台一次也没有生产过，是新机台，直接返回所有工位，不跳过的所有工位
        if (oldMesRecordWork == null) {
            return true;
        }
        MesMoSchedule old = mesMoScheduleRepository.findById(oldMesRecordWork.getScheduleId()).orElse(null);
        //比较当前机台处理的排产单的料件id和上一次排产单的料件id
        if (old != null && old.getMachineId().equals(current.getMachineId())) {
            //如果相同，工位排除掉架模，调机，首件，返回跳过的工位
            return false;
        }
        return true;
    }

    /**
     * 获取待处理的所有工位
     *
     * @param staffId
     * @param scheduleId
     * @return
     */
    private List<PadStationModel> getAllBaseStations(String staffId, String scheduleId) {
        String sqlStation = "SELECT\n" +
                            "	mss.process_id processId,\n" +
                            "	bs.station_id stationId,\n" +
                            "	bs.code code,\n" +
                            "	bs.name name,\n" +
                            "	mps.step step\n" +
                            "FROM\n" +
                            "	base_station bs,\n" +
                            "	mes_mo_schedule_staff mss,\n" +
                            "	base_process_station mps\n" +
                            "WHERE\n" +
                            "	mss.station_id = bs.station_id\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n" +
                            "AND mps.station_id = mss.station_id\n" +
                            "AND mps.process_id = mss.process_id\n" +
                            "ORDER BY\n" +
                            "	mps.step ASC";
        RowMapper<PadStationModel> rowMapper = BeanPropertyRowMapper.newInstance(PadStationModel.class);
        return jdbcTemplate.query(sqlStation, rowMapper);
    }

    /**
     * 获取待处理的所有已排除跳过的工位
     *
     * @param staffId
     * @param scheduleId
     * @return
     */
    private List<PadStationModel> getAllExcludedBaseStations(String staffId, String scheduleId) {
        String sqlStation = "SELECT\n" +
                            "	mss.process_id processId,\n" +
                            "	bs.station_id stationId,\n" +
                            "	bs.code code,\n" +
                            "	bs.name name,\n" +
                            "	mps.step step\n" +
                            "FROM\n" +
                            "	base_station bs,\n" +
                            "	mes_mo_schedule_staff mss,\n" +
                            "	base_process_station mps,\n" +
                            "	mes_mo_schedule_station mmss\n" +
                            "WHERE\n" +
                            "	mss.station_id = bs.station_id\n" +
                            "AND mss.schedule_id = '" + scheduleId + "'\n" +
                            "AND mss.staff_id = '" + staffId + "'\n" +
                            "AND mps.station_id = mss.station_id\n" +
                            "AND mps.process_id = mss.process_id\n" +
                            "AND mss.schedule_id = mmss.schedule_id\n" +
                            "AND mss.station_id = mmss.station_id\n" +
                            "AND mmss.jump = 0\n" +
                            "ORDER BY\n" +
                            "	mps.step ASC";
        RowMapper<PadStationModel> rowMapper = BeanPropertyRowMapper.newInstance(PadStationModel.class);
        return jdbcTemplate.query(sqlStation, rowMapper);
    }

    @Override
    public StationAndOperate getStationsAndOperate(String scheduleId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StationAndOperate stationAndOperate = new StationAndOperate();
        List<PadStationModel> pendingStations = getPendingStations(scheduleId);
        stationAndOperate.setPadStationModels(pendingStations);
        if(pendingStations==null||pendingStations.size()==0){
            OperationInfo operationInfo = new OperationInfo();
            //上工标志位/下工标志位(0:上工,1:下工)
            operationInfo.setWorkFlag("0");
            //上下工(0:置灰,1:不置灰)
            operationInfo.setStartWork("0");
            //不良品数(0:置灰,1:不置灰)
            operationInfo.setDefectiveProducts("0");
            //提报异常(0:置灰,1:不置灰)
            operationInfo.setReportingAnomalies("0");
            //作业输入(0:置灰,1:不置灰)
            operationInfo.setJobInput("0");
            //作业指导(0:置灰,1:不置灰)
            operationInfo.setHomeworkGuidance("0");
            //操作历史(0:置灰,1:不置灰)
            operationInfo.setOperationHistory("0");
            //结束作业(0:置灰,1:不置灰)
            operationInfo.setFinishHomework("0");
            stationAndOperate.setOperationInfo(operationInfo);
            return stationAndOperate;
        }
        OperationInfo operationInfo = padDispatchService.getOperationInfo(scheduleId, pendingStations.get(0).getStationId());
        stationAndOperate.setOperationInfo(operationInfo);
        return stationAndOperate;
    }


}
