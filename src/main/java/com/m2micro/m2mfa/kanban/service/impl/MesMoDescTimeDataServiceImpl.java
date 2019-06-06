package com.m2micro.m2mfa.kanban.service.impl;

import com.m2micro.m2mfa.kanban.service.MesMoDescTimeDataService;
import com.m2micro.m2mfa.kanban.vo.MesMoDescAndProcess;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTime;
import com.m2micro.m2mfa.kanban.vo.MesMoDescTimeData;
import com.m2micro.m2mfa.kanban.vo.ProcessData;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.*;

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
    Set<String>processnames= new TreeSet<>(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o2.compareTo(o1);//降序排列
      }
    });
    for(MesMoDescAndProcess x:mesMoDescAndProcesses){
      MesMoDescTimeData mesMoDescTimeData = new MesMoDescTimeData();
      mesMoDescTimeData.setMoNumber(x.getMoNumber());
      mesMoDescTimeData.setCustomerName(x.getCustomerName());
      mesMoDescTimeData.setPartName(x.getPartName());
      mesMoDescTimeData.setPartNo(x.getPartNo());
      mesMoDescTimeData.setCloseFlag(MoStatus.valueOf(Integer.parseInt(x.getCloseFlag())).getValue());
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
        "	IFNULL(SUM(vmpi.output_qty),0)\t  process_output_qty \n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd on mmd.mo_id=vmpi.mo_id\n" +
        "LEFT JOIN base_customer  bc on bc.customer_id=mmd.customer_id\n" +
        "LEFT JOIN base_parts bp   on bp.part_id=mmd.part_id\n" +
        "LEFT JOIN base_process bpd  on  bpd.process_id= vmpi.process_id\n"+
        "where   mmd.mo_number='"+moNumber+"'  and bpd.process_name= '"+processName+"'";
        return  jdbcTemplate.queryForObject(sql,long.class);
  }


  private List<MesMoDescAndProcess> getMesMoDescAndProcesses() {
    String sql ="SELECT\n" +
        "	mmd.mo_number,\n" +
        "  bc.`name` customer_name,\n" +
        "	#vmpi.schedule_no,\n" +
        "	bp.`name` process_name,\n" +
        "	bp.part_no part_no,\n" +
        "	mmd.reach_date,\n" +
        "	mmd.close_flag,\n" +
        "	mmd.target_qty  mes_mo_desc_target_qty,\n" +
        "  IFNULL(mmd.output_qty,0)  mes_mo_desc_output_qty,\n" +
        "	vmpi.output_qty  process_output_qty,\n" +
        "	bp.`name` process_name \n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd on mmd.mo_id=vmpi.mo_id\n" +
        "LEFT JOIN base_customer  bc on bc.customer_id=mmd.customer_id\n" +
        "LEFT JOIN base_parts bp   on bp.part_id=mmd.part_id  where   mmd.close_flag="+ MoStatus.PRODUCTION.getKey() +"  \n" +
        "ORDER BY\n" +
        "	mmd.mo_number , vmpi.process_id";
    RowMapper<MesMoDescAndProcess> rowMapper = BeanPropertyRowMapper.newInstance(MesMoDescAndProcess.class);
    return jdbcTemplate.query(sql,rowMapper);
  }


}
