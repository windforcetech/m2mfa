package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.base.entity.BaseStaff;
import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.base.repository.BaseStaffRepository;
import com.m2micro.m2mfa.mo.constant.MoScheduleStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.model.OperationInfo;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.InitData;
import com.m2micro.m2mfa.pad.model.PadScheduleModel;
import com.m2micro.m2mfa.pad.model.PadStationModel;
import com.m2micro.m2mfa.pad.service.PadScheduleService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public InitData getInitData(String staffId) {
        InitData initData = new InitData();
        MesMoSchedule mesMoSchedule = mesMoScheduleService.getMesMoScheduleByStaffId(staffId);
        if(mesMoSchedule!=null){
            initData.setMesMoSchedule(mesMoSchedule);
            initData.setBaseStations(mesMoScheduleService.getPendingStations(staffId, mesMoSchedule.getScheduleId()));
        }
        return initData;
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
        //获取该员工下已审待产和生产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        String sql = "SELECT\n" +
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
                "AND (ms.flag = "+ MoScheduleStatus.AUDITED.getKey() +" OR ms.flag = "+ MoScheduleStatus.PRODUCTION.getKey() +")\n" +
                "AND bm.machine_id = ms.machine_id\n" +
                "AND mss.staff_id = '"+ baseStaff.getStaffId() + "'\n" +
                "GROUP BY\n" +
                "	ms.machine_id\n" +
                "ORDER BY\n" +
                "	ms.sequence ASC,\n" +
                "	bm.code ASC";
        RowMapper<PadScheduleModel> rowMapper = BeanPropertyRowMapper.newInstance(PadScheduleModel.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<PadScheduleModel> getMesMoSchedule() {
        return getMesMoScheduleByBaseStaff(PadStaffUtil.getStaff());
    }

    @Override
    public List<PadStationModel> getPendingStations(String staffNo, String scheduleId) {
        BaseStaff baseStaff = baseStaffRepository.findByCode(staffNo);
        if(baseStaff==null){
            throw new MMException("不存在该员工");
        }
        //获取该员工下已审待产和生产中每个机器上的排产单（每个机器只取优先级最高的排产单）
        String sql = "SELECT\n" +
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
                        "AND mss.schedule_id = '"+ scheduleId + "'\n" +
                        "AND mss.staff_id = '"+ baseStaff.getStaffId() + "'\n" +
                        "AND mps.station_id = mss.station_id\n" +
                        "AND mps.process_id = mss.process_id\n" +
                        "AND mss.schedule_id = mmss.schedule_id\n" +
                        "AND mss.station_id = mmss.station_id\n" +
                        "AND mmss.jump = 1\n" +
                        "ORDER BY\n" +
                        "	mps.step ASC";
        RowMapper<PadStationModel> rowMapper = BeanPropertyRowMapper.newInstance(PadStationModel.class);
        return jdbcTemplate.query(sql, rowMapper);

    }

    @Override
    public OperationInfo getOperationInfo(String staffId, String scheduleId, String stationId) {
        return mesMoScheduleService.getOperationInfo(staffId, scheduleId, stationId);
    }
}
