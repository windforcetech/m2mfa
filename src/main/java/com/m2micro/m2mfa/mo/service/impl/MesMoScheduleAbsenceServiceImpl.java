package com.m2micro.m2mfa.mo.service.impl;

import com.m2micro.framework.commons.exception.MMException;
import com.m2micro.m2mfa.common.util.UUIDUtil;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleRepository;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoScheduleAbsenceService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleStaffService;
import com.m2micro.m2mfa.mo.vo.Absence;
import com.m2micro.m2mfa.mo.vo.AbsencePersonnel;
import com.m2micro.m2mfa.record.entity.MesRecordStaff;
import com.m2micro.m2mfa.record.repository.MesRecordStaffRepository;
import com.m2micro.m2mfa.record.service.MesRecordStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MesMoScheduleAbsenceServiceImpl implements MesMoScheduleAbsenceService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MesRecordStaffRepository mesRecordStaffRepository;
    @Autowired
    MesRecordStaffService mesRecordStaffService;
    @Autowired
    MesMoScheduleRepository mesMoScheduleRepository;
    @Autowired
    MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
    @Autowired
    MesMoScheduleStaffService mesMoScheduleStaffService;
    @Override
    public List<Absence> mesMoScheduleAbsence(String staffId) {
        String sql ="SELECT bm. CODE machineCode, mms.schedule_no scheduleNo,mms.schedule_id scheduleId, bp.part_no partNo, bps.process_name processName, bsson. NAME stationName, mmss.create_on createTime, bsft. NAME shiftName FROM mes_mo_schedule_staff mmss LEFT JOIN mes_mo_schedule mms ON mmss.schedule_id = mms.schedule_id LEFT JOIN base_staff bs ON bs.staff_id = mmss.staff_id LEFT JOIN base_machine bm ON bm.machine_id = mms.machine_id LEFT JOIN organization o ON bm.department_id = o.uuid LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = mms.mo_id LEFT JOIN base_parts bp ON bp.part_id = mmd.part_id LEFT JOIN base_process bps ON bps.process_id = mmss.process_id LEFT JOIN base_station bsson ON bsson.station_id = mmss.station_id LEFT JOIN base_shift bsft ON bsft.shift_id = mmss.shift_id WHERE mmss.staff_id = '"+staffId+"' AND mmss.actual_start_time IS NULL OR ( mmss.actual_start_time IS NOT NULL AND mmss.actual_end_time IS NULL ) GROUP BY mms.schedule_id AND mms.flag = "+ MoStatus.AUDITED.getKey()+" OR mms.flag = "+MoStatus.SCHEDULED.getKey()+"";
        RowMapper rm = BeanPropertyRowMapper.newInstance(Absence.class);
        return jdbcTemplate.query(sql,rm);
    }
    @Transactional
    @Override
    public void save(AbsencePersonnel absencePersonnel) {
                List<MesRecordStaff>mesRecordStaffs =  mesRecordStaffRepository.findStaffId(absencePersonnel.getLackstaffId());
                if(!mesRecordStaffs.isEmpty()){
                    for(MesRecordStaff mesRecordStaff:mesRecordStaffs){
                        mesRecordStaff.setEndTime(new Date());
                        mesRecordStaffService.updateById(mesRecordStaff.getId(),mesRecordStaff);
                        //需要后期的一些结算数量操作
                    }

                }
                String []   ScheduleIds= absencePersonnel.getScheduleIds();
                for(int i=0; i<ScheduleIds.length;i++){
                    String ScheduleId =ScheduleIds[i];
                    if(ScheduleId==null || mesMoScheduleRepository.findById(ScheduleId).orElse(null)==null){
                        throw  new MMException("排产单ID有误。");
                    }
                        //缺勤替换
                        List<MesMoScheduleStaff>mesMoScheduleStaffs =  mesMoScheduleStaffRepository.findByScheduleIdandStafftId(ScheduleId,absencePersonnel.getLackstaffId());
                        for(MesMoScheduleStaff mesMoScheduleStaff :mesMoScheduleStaffs){
                            MesMoScheduleStaff newmesMoScheduleStaff= new MesMoScheduleStaff();
                            newmesMoScheduleStaff.setId(UUIDUtil.getUUID());
                            newmesMoScheduleStaff.setStaffId(absencePersonnel.getForstaffId());
                            newmesMoScheduleStaff.setScheduleId(mesMoScheduleStaff.getScheduleId());
                            newmesMoScheduleStaff.setStationId(mesMoScheduleStaff.getStationId());
                            newmesMoScheduleStaff.setIsStation(mesMoScheduleStaff.getIsStation());
                            newmesMoScheduleStaff.setShiftId(mesMoScheduleStaff.getShiftId());
                            newmesMoScheduleStaff.setProcessId(mesMoScheduleStaff.getProcessId());
                            newmesMoScheduleStaff.setEnabled(true);
                            mesMoScheduleStaffService.save(newmesMoScheduleStaff);
                        }

        }

    }
}
