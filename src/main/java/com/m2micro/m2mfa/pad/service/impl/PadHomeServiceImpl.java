package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.starter.services.OrganizationService;
import com.m2micro.m2mfa.base.entity.*;
import com.m2micro.m2mfa.base.service.*;
import com.m2micro.m2mfa.iot.entity.IotMachineOutput;
import com.m2micro.m2mfa.iot.service.IotMachineOutputService;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.repository.MesMoScheduleStaffRepository;
import com.m2micro.m2mfa.mo.service.MesMoDescService;
import com.m2micro.m2mfa.mo.service.MesMoScheduleService;
import com.m2micro.m2mfa.pad.model.PadHomeModel;
import com.m2micro.m2mfa.pad.model.PadHomePara;
import com.m2micro.m2mfa.pad.service.PadHomeService;
import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import com.m2micro.m2mfa.pr.repository.MesPartRouteRepository;
import com.m2micro.m2mfa.pr.repository.MesPartRouteStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

@Service
public class PadHomeServiceImpl  implements PadHomeService {
  @Autowired
  private MesMoScheduleService mesMoScheduleService;
  @Autowired
  private MesMoScheduleStaffRepository mesMoScheduleStaffRepository;
  @Autowired
  private MesPartRouteStationRepository mesPartRouteStationRepository;
  @Autowired
  private MesPartRouteRepository mesPartRouteRepository;
  @Autowired
  private MesMoDescService mesMoDescService;
  @Autowired
  BaseMachineService baseMachineService;
  @Autowired
  private BaseProcessService baseProcessService;
  @Autowired
  private BaseItemsTargetService baseItemsTargetService;
  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private IotMachineOutputService iotMachineOutputService;
  @Autowired
  private BaseStaffService baseStaffService;
  @Autowired
  private OrganizationService organizationService;
  @Autowired
  private BaseShiftService baseShiftService;
  @Override
  public PadHomeModel findByHome(PadHomePara padHomePara) {

    //显示机台信息及原料使用情况
    //获取排产单
    MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(padHomePara.getScheduleId()).orElse(null);
    //作业人员信息
    List<MesMoScheduleStaff>mesMoScheduleStaffs = mesMoScheduleStaffRepository.findByScheduleIdandStafftId(mesMoSchedule.getScheduleId(),padHomePara.getStaffId());


    MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);
    MesPartRoute mesPartRoute = mesPartRouteRepository.findByPartId(moDesc.getPartId()).get(0);
   //获取机台信息
    BaseMachine baseMachine = baseMachineService.findById(mesMoSchedule.getMachineId()).orElse(null);
    //工序信息
    BaseProcess baseProcess = baseProcessService.findById(padHomePara.getProcessId()).orElse(null);

    //执行标准
    MesPartRouteStation mesPartRouteStation = mesPartRouteStationRepository.findByPartRouteIdAndProcessIdAndStationId(mesPartRoute.getPartRouteId(),padHomePara.getProcessId(),padHomePara.getStationId()).get(0);

    //机台产量信息
    IotMachineOutput iotMachineOutput  = iotMachineOutputService.findIotMachineOutputByMachineId(baseMachine.getMachineId());

    //职员基本信息
    BaseStaff baseStaff= baseStaffService.findById(padHomePara.getStaffId()).orElse(null);
    //班别资料
    BaseShift baseShift =baseShiftService.findById(mesMoScheduleStaffs.get(0).getShiftId()).orElse(null);

    //职员上班工时
    long hours = baseShiftService.findbhours(baseShift.getShiftId());

    BigDecimal standardHours = mesPartRouteStation.getStandardHours();
    BigDecimal bdhours = new BigDecimal(hours);
    BigDecimal standardOutput = bdhours.divide(standardHours, 2, RoundingMode.HALF_UP);
    BigDecimal actualOutput =iotMachineOutput.getOutput();
    Integer partInput = partInput(padHomePara.getRwId());
    Integer partOutput = 0;
    NumberFormat nt = NumberFormat.getPercentInstance();
    nt.setMinimumFractionDigits(0);
    float rate = (float)actualOutput.longValue()/standardOutput.longValue();
    return PadHomeModel.builder().staffCode(baseStaff.getCode()).staffName(baseStaff.getStaffName()).staffDepartmentName(organizationService.findByUUID(baseStaff.getDepartmentId()).getDepartmentName())
        .staffShiftName(baseShift.getName()).staffOnTime(onTime(padHomePara.getStaffId())).standardOutput(standardOutput.longValue()).actualOutput(actualOutput.longValue()).machineName(baseMachine.getName()).collection(baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null).getItemName())
        .partInput(partInput).partOutput(partOutput).partRemaining((partInput-partOutput)).rate(rate).build();
  }


  /**
   * 获取加料总和
   * @param rwid
   * @return
   */
  public Integer partInput(String rwid){
    String sql ="select IFNULL(SUM(input_size),0 ) from mes_record_lotparts where rw_id='"+rwid+"'";
    return  jdbcTemplate.queryForObject(sql,Integer.class);
  }

  /**
   * 获取职业最新上岗时间
   * @param staffId
   * @return
   */
  public Date  onTime(String  staffId){
    String sql ="select start_time from mes_record_staff where  staff_id='"+staffId+"' order by start_time desc LIMIT 0,1";
    return  jdbcTemplate.queryForObject(sql,Date.class);
  }
}
