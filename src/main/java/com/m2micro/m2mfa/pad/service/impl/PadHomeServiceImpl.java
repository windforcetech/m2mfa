package com.m2micro.m2mfa.pad.service.impl;

import com.m2micro.framework.commons.exception.MMException;
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
import com.m2micro.m2mfa.pad.model.PadYieldPara;
import com.m2micro.m2mfa.pad.service.PadHomeService;
import com.m2micro.m2mfa.pad.util.PadStaffUtil;
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
    List<MesMoScheduleStaff>mesMoScheduleStaffs = mesMoScheduleStaffRepository.findByScheduleIdandStafftId(mesMoSchedule.getScheduleId(),PadStaffUtil.getStaff().getStaffId());

    MesMoDesc moDesc = mesMoDescService.findById(mesMoSchedule.getMoId()).orElse(null);

    MesPartRoute mesPartRoute = mesPartRouteRepository.findByPartId(moDesc.getPartId()).get(0);
   //获取机台信息
    BaseMachine baseMachine = baseMachineService.findById(mesMoSchedule.getMachineId()).orElse(null);
    //工序信息
    BaseProcess baseProcess = baseProcessService.findById(padHomePara.getProcessId()).orElse(null);

    //执行标准
    List<MesPartRouteStation> mesPartRouteStations = mesPartRouteStationRepository.findByPartRouteIdAndProcessIdAndStationId(mesPartRoute.getPartRouteId(), padHomePara.getProcessId(), padHomePara.getStationId());
    if(mesPartRouteStations.isEmpty()){
      throw  new MMException("工位有误");
    }

    MesPartRouteStation mesPartRouteStation =mesPartRouteStations.get(0);
    //机台产量信息
    IotMachineOutput iotMachineOutput  = iotMachineOutputService.findIotMachineOutputByMachineId(baseMachine.getMachineId());

    //职员基本信息
    BaseStaff baseStaff= baseStaffService.findById(PadStaffUtil.getStaff().getStaffId()).orElse(null);
    //班别资料
    BaseShift baseShift =baseShiftService.findById(mesMoScheduleStaffs.get(0).getShiftId()).orElse(null);

    String rwId =newRwid(padHomePara.getScheduleId(),padHomePara.getStationId());
    //获取职员上工时间
    Date  startTime = startTime(rwId,PadStaffUtil.getStaff().getStaffId());
    BigDecimal standardOutput = new BigDecimal(0);
    BigDecimal actualOutput  = new BigDecimal(0);
    double rate=0.00;
    if(startTime !=null){
      BigDecimal standardHours = mesPartRouteStation.getStandardHours();
      BigDecimal bdhours = new BigDecimal((new Date().getTime()-startTime.getTime())/1000);
      if(standardHours.compareTo(BigDecimal.ZERO)!=0){
        standardOutput = bdhours.divide(standardHours, 2, RoundingMode.HALF_UP);
      }

      //获取当前员工开始模数
      BigDecimal startMolds=startMolds(rwId,PadStaffUtil.getStaff().getStaffId());
      //实际产出
      if(startMolds !=null && startMolds.compareTo(BigDecimal.ZERO)!=0){
        actualOutput =startMolds==null ? new  BigDecimal(0) :(iotMachineOutput.getOutput().subtract(startMolds));
      }

       //达成率
      rate = actualOutput.doubleValue()/standardOutput.longValue();
    }

    Integer partInput = partInput(rwId);
    Integer partOutput = 0;
    return PadHomeModel.builder().staffCode(baseStaff.getCode()).staffName(baseStaff.getStaffName()).staffDepartmentName(organizationService.findByUUID(baseStaff.getDepartmentId()).getDepartmentName())
        .staffShiftName(baseShift.getName()).staffOnTime(startTime).standardOutput(standardOutput.longValue()).actualOutput(actualOutput.longValue()).machineName(baseMachine.getName()).collection(baseItemsTargetService.findById(baseProcess.getCollection()).orElse(null).getItemName())
        .partInput(partInput).partOutput(partOutput).partRemaining((partInput-partOutput)).rate( (long)( rate*100)).build();
  }


  public static void main(String args[]) {
    BigDecimal standardOutput = new BigDecimal(5);
    BigDecimal actualOutput  = new BigDecimal(50);
   double  rate = actualOutput.doubleValue()/standardOutput.doubleValue();
   System.out.println((long)( rate*100));
  }

  @Override
  public Boolean isScheduleYield(PadYieldPara padHomePara) {
    //获取排产单
    MesMoSchedule mesMoSchedule = mesMoScheduleService.findById(padHomePara.getScheduleId()).orElse(null);
    //获取机台信息
    BaseMachine baseMachine = baseMachineService.findById(mesMoSchedule.getMachineId()).orElse(null);
    //机台产量信息
    IotMachineOutput iotMachineOutput  = iotMachineOutputService.findIotMachineOutputByMachineId(baseMachine.getMachineId());
    String rwId =newRwid(padHomePara.getScheduleId(),padHomePara.getStationId());
      //获取当前员工开始模数
    BigDecimal startMolds=startMolds(rwId,PadStaffUtil.getStaff().getStaffId());
      //实际产出
    BigDecimal  actualOutput =startMolds==null ? new  BigDecimal(0) :(iotMachineOutput.getOutput().subtract(startMolds));
    return padHomePara.getTotalamount()>actualOutput.longValue() ? true : false;
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


  /**
   * 获取工位的最新上工记录
   * @param scheduleId
   * @param stationId
   * @return
   */
  public String newRwid(String scheduleId ,String stationId){
    String sql ="select rwid  from mes_record_work mrw where schedule_id='"+scheduleId+"' and station_id ='"+stationId+"'    order by  start_time   desc limit 1 ";
    try {
      return jdbcTemplate.queryForObject(sql, String.class);
     }catch (Exception e) {
      return null;
      }
  }

  /**
   * 获取当前员工上工的开始模数
   * @param rwId
   * @param staffId
   * @return
   */
  public BigDecimal startMolds(String rwId,String staffId){
    String sql ="SELECT IFNULL(start_molds,0) FROM  mes_record_staff where rw_id='"+rwId+"' and  staff_id='"+staffId+"' and start_time is NOT null and end_time is NULL";
    try {
      return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }catch (Exception e) {
      return null ;
    }
  }

  /**
   * 获取当前员工上工时间
   * @param rwId
   * @param staffId
   * @return
   */
  public Date startTime(String rwId,String staffId){
    String sql ="select start_time  from  mes_record_staff  where rw_id='"+rwId+"' and  staff_id='"+staffId+"' and start_time is NOT null and end_time is NULL";
    try {
      return jdbcTemplate.queryForObject(sql, Date.class);
    }catch (Exception e) {
      return null ;
    }
  }


}


