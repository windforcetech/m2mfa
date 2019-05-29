package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.m2mfa.kanban.service.MesMoDescTimeDataService;
import com.m2micro.m2mfa.kanban.vo.MesMoDescAndProcess;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTime;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTimeData;
import com.m2micro.m2mfa.kanban.vo.ProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MesMoDescTimeDataServiceImpl implements MesMoDescTimeDataService {

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public MesMoDescTime MesMoDescTimeDataShow() {
    MesMoDescTime mesMoDescTime = new MesMoDescTime();
    List<MesMoDescAndProcess> mesMoDescAndProcesses = getMesMoDescAndProcesses();
    Set<MesMoDescTimeData> mesMoDescTimeDatas =new HashSet<>();
    Set<String>processnames= new HashSet<>();
    for(MesMoDescAndProcess x:mesMoDescAndProcesses){
      MesMoDescTimeData mesMoDescTimeData = new MesMoDescTimeData();
      mesMoDescTimeData.setMoNumber(x.getMoNumber());
      mesMoDescTimeData.setCustomerName(x.getCustomerName());
      mesMoDescTimeData.setPartName(x.getPartName());
      mesMoDescTimeData.setReachDate(x.getReachDate());
      mesMoDescTimeData.setMesMoDescTargetQty(x.getMesMoDescTargetQty());
      mesMoDescTimeData.setMesMoDescOutputQty(x.getMesMoDescOutputQty());
      processnames.add(x.getProcessName());
      mesMoDescTimeDatas.add(mesMoDescTimeData);
    }

    for( MesMoDescTimeData x: mesMoDescTimeDatas){
      Set<ProcessData> processDatas = new HashSet<>();
      for(MesMoDescAndProcess y:mesMoDescAndProcesses){
        if(x.getMoNumber().equals(y.getMoNumber())){
          ProcessData processData = new ProcessData();
          processData.setProcessName(y.getProcessName());
          processData.setProcessOutputQty(getProcessOutputQty(x.getMoNumber(),y.getProcessName()));
          processDatas.add(processData);
        }
      }
      x.setProcessDatas(processDatas);
    }
    mesMoDescTime.setMesMoDescTimeDatas(mesMoDescTimeDatas);
    mesMoDescTime.setProcessnames(processnames);
    return mesMoDescTime;
  }


  private long  getProcessOutputQty(String moNumber,String processName){
    String sql ="SELECT\n" +
        "	SUM(vmpi.output_qty)  process_output_qty \n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd on mmd.mo_id=vmpi.mo_id\n" +
        "LEFT JOIN base_customer  bc on bc.customer_id=mmd.customer_id\n" +
        "LEFT JOIN base_parts bp   on bp.part_id=mmd.part_id\n" +
        "where   mmd.mo_number='"+moNumber+"'  and process_name= '"+processName+"'";
        return  jdbcTemplate.queryForObject(sql,long.class);
  }


  private List<MesMoDescAndProcess> getMesMoDescAndProcesses() {
    String sql ="SELECT\n" +
        "	mmd.mo_number,\n" +
        "  bc.`name` customer_name,\n" +
        "	#vmpi.schedule_no,\n" +
        "	bp.`name` part_name,\n" +
        "	mmd.reach_date,\n" +
        "	mmd.target_qty  mes_mo_desc_target_qty,\n" +
        "  IFNULL(mmd.output_qty,0)  mes_mo_desc_output_qty,\n" +
        "	vmpi.output_qty  process_output_qty,\n" +
        "	vmpi.process_name \n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd on mmd.mo_id=vmpi.mo_id\n" +
        "LEFT JOIN base_customer  bc on bc.customer_id=mmd.customer_id\n" +
        "LEFT JOIN base_parts bp   on bp.part_id=mmd.part_id\n" +
        "ORDER BY\n" +
        "	vmpi.mo_number , vmpi.process_id";
    RowMapper<MesMoDescAndProcess> rowMapper = BeanPropertyRowMapper.newInstance(MesMoDescAndProcess.class);
    return jdbcTemplate.query(sql,rowMapper);
  }


}
