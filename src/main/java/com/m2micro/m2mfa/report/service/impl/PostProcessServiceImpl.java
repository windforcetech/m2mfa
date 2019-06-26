package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.report.service.PostProcessService;
import com.m2micro.m2mfa.report.vo.PostProcess;
import com.m2micro.m2mfa.report.vo.PostProcessAndData;
import com.m2micro.m2mfa.report.vo.PostProcessQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostProcessServiceImpl implements PostProcessService {

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;


  @Override
  public PostProcessAndData PostProcessShow(PostProcessQuery postProcessQuery) {

    String sql ="select mrwl.out_time,\n" +
        " sum(IFNULL(mrwl.output_qty,0))+SUM((SELECT IFNULL(SUM(mrf.fail_qty),0) from mes_record_wip_fail mrf where mrf.schedule_id = mrwl.schedule_id and  mrwl.staff_id=mrf.staff_id) )  output_qty,\n" +
        " TRUNCATE( (sum(IFNULL(mrwl.output_qty,0))+SUM((SELECT IFNULL(SUM(mrf.fail_qty),0) from mes_record_wip_fail mrf where mrf.schedule_id = mrwl.schedule_id and  mrwl.staff_id=mrf.staff_id) )) / TRUNCATE(((max(mrwl.out_time))-(MIN(out_time))) /sum(mmstation.standard_hours)  ,2)*100 ,2) achieving_rate,\n" +
        " TRUNCATE(((SELECT IFNULL(SUM(mrf.fail_qty),0) from mes_record_wip_fail mrf where mrf.schedule_id = mrwl.schedule_id and  mrwl.staff_id=mrf.staff_id)  /  SUM( IFNULL(mrwl.output_qty,0))) *100,2) fail_qty_rate,\n" +
        " ((max(mrwl.out_time))-(MIN(out_time))) workinghours,\n" +
        " TRUNCATE(sum(IFNULL(mrwl.output_qty,0)) /COUNT(*),0) staff_average";
    sql +=pingSql(postProcessQuery);
    RowMapper<PostProcessAndData> rowMapper = BeanPropertyRowMapper.newInstance(PostProcessAndData.class);
    PostProcessAndData postProcessAndData = jdbcTemplate.queryForObject(sql, rowMapper);

    sql = "SELECT \n" +
        "  mrwl.out_time,\n" +
        "  bp.process_name,\n" +
        "  bps.part_no,\n" +
        "  bps.`name` part_name,\n" +
        "  bs.`name` shift_name,\n" +
        "  bss.`code`  staff_code,\n" +
        "  bss.staff_name,\n" +
        " ((max(mrwl.out_time))-(MIN(out_time)))  workinghours,\n" +
        " sum(IFNULL(mrwl.output_qty,0)) output_qty,\n" +
        "TRUNCATE(((max(mrwl.out_time))-(MIN(out_time))) /sum(mmstation.standard_hours)  ,2) reach_number,\n" +
        "TRUNCATE(( sum(IFNULL(mrwl.output_qty,0)) / TRUNCATE(((max(mrwl.out_time))-(MIN(out_time))) /sum(mmstation.standard_hours)  ,2))*100 , 2) achieving_rate,\n" +
        "(SELECT IFNULL(SUM(mrf.scrap_qty),0) from mes_record_wip_fail mrf where mrf.schedule_id = mrwl.schedule_id and  mrwl.staff_id=mrf.staff_id) scrap_qty,\n" +
        "TRUNCATE( (SELECT IFNULL(SUM(mrf.fail_qty),0) from mes_record_wip_fail mrf where mrf.schedule_id = mrwl.schedule_id and  mrwl.staff_id=mrf.staff_id)  /  sum(IFNULL(mrwl.output_qty,0)) * 100,2) fail_qty_rate,\n" +
        " bm.`name`  mold_name";
    sql +=pingSql(postProcessQuery);
    sql +="  GROUP BY mrwl.process_id , mms.shift_id , mrwl.staff_id \n" +
        " ORDER BY mrwl.process_id , mms.shift_id , mrwl.staff_id \n";
    RowMapper<PostProcess> postProcessMapper = BeanPropertyRowMapper.newInstance(PostProcess.class);
    List<PostProcess> postProcesses = jdbcTemplate.query(sql, postProcessMapper);
    if(postProcessAndData.getOutTime() ==null){
      return null;
    }
    List<PostProcess> postProcessesdata = postProcesses.stream().filter(x -> {
      x.setWorkinghours(DateUtil.getGapTime(Long.parseLong(x.getWorkinghours())));
      return true;
    }).collect(Collectors.toList());
    postProcessAndData.setPostProcesses(postProcessesdata);
    postProcessAndData.setWorkinghours(DateUtil.getGapTime(Long.parseLong(postProcessAndData.getWorkinghours())));
    return postProcessAndData;
  }

  /**
   * 共同的sql
   * @param postProcessQuery
   * @return
   */
  public String pingSql(PostProcessQuery postProcessQuery){
    String sql ="  FROM\n" +
        "	mes_record_wip_log mrwl\n" +
        "LEFT JOIN mes_mo_desc mmd  on mmd.mo_id=mrwl.mo_id\n" +
        "LEFT JOIN base_process bp  ON  bp.process_id =mrwl.process_id\n" +
        "LEFT JOIN base_parts bps on  bps.part_id = mrwl.part_id\n" +
        "LEFT JOIN  mes_mo_schedule_staff mms on mrwl.schedule_id=mms.schedule_id  and  mms.staff_id=mrwl.staff_id\n" +
        "LEFT JOIN  base_shift bs  on bs.shift_id=mms.shift_id\n" +
        "LEFT JOIN base_staff bss   on bss.staff_id=mrwl.staff_id\n" +
        "LEFT JOIN mes_mo_schedule_station  mmstation on mmstation.schedule_id=mms.schedule_id and mrwl.process_id=mmstation.process_id\n" +
        "LEFT JOIN mes_mo_schedule_process  mmsc on mmsc.schedule_id = mms.schedule_id and NOT ISNULL(mmsc.mold_id)\n" +
        "LEFT JOIN base_mold bm on bm.mold_id=mmsc.mold_id"+
        "   where 1=1   and mrwl.out_time  LIKE '"+ DateUtil.format(postProcessQuery.getPostProcessTime(),DateUtil.DATE_PATTERN)+"%'  ";

    return sql;

  }
}
