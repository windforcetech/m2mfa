package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.report.query.MachineProduceQuery;
import com.m2micro.m2mfa.report.service.MachineProduceService;
import com.m2micro.m2mfa.report.vo.MachineProduce;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class MachineProduceServiceImpl implements MachineProduceService {


  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<MachineProduce > MachineProduceShow(MachineProduceQuery machineProduceQuery) {
    String sql =" \n" +
        "SELECT  \n" +
        "	bm.`name` machine_name,\n" +
        "	bs.`name` shift_name,\n" +
        "	bp.part_no,\n" +
        "	bp.`name` part_name,\n" +
        "	bmld.`name` mold_name,\n" +
        "	bp.`name` customer_part_no,\n" +
        "	bp.grade material,\n" +
        "	(	SELECT COUNT(*) FROM mes_record_staff mrs WHERE mrs.rw_id = mrw.rwid) staff_number,\n" +
        "	mmss.standard_hours,\n" +
        "	CASE\n" +
        "WHEN mrw.end_time IS NULL THEN\n" +
        "	(NOW() - mrw.start_time)\n" +
        "ELSE\n" +
        "	(mrw.end_time - mrw.start_time)\n" +
        "END machine_time_cost,\n" +
        " bmld.cavity_qty,\n" +
        " bmld.cavity_available,\n" +
        " IFNULL(mms.schedule_qty, 0) schedule_qty,\n" +
        " CASE\n" +
        "WHEN mrw.end_time IS NULL THEN\n" +
        "(	IFNULL(imo.molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "ELSE\n" +
        "(IFNULL(mrw.end_molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "END machine_molds,\n" +
        "(CASE WHEN mrw.end_time IS NULL THEN\n" +
        "(IFNULL(imo.molds, 0.00) - IFNULL(mrw.start_molds, 0.00)	)\n" +
        "ELSE\n" +
        "(IFNULL(mrw.end_molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "END\n" +
        ") - IFNULL((SELECT	SUM(mrf.qty) FROM mes_record_fail mrf WHERE mrf.rw_id = mrw.rwid),0) good_product_number,\n" +
        "IFNULL((SELECT	SUM(mrf.qty) FROM  mes_record_fail mrf WHERE mrf.rw_id = mrw.rwid),0) fail_qty,\n" +
        "IFNULL(TRUNCATE ((((\n" +
        "CASE\n" +
        "WHEN mrw.end_time IS NULL THEN(IFNULL(imo.molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "ELSE\n" +
        "(IFNULL(mrw.end_molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "END\n" +
        ") - IFNULL((SELECT	SUM(mrf.qty) FROM	mes_record_fail mrf  WHERE	mrf.rw_id = mrw.rwid),0)) / (\n" +
        "CASE\n" +
        "WHEN mrw.end_time IS NULL THEN\n" +
        "(IFNULL(imo.molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "ELSE\n" +
        "(IFNULL(mrw.end_molds, 0.00) - IFNULL(mrw.start_molds, 0.00))\n" +
        "END)) * 100,2)  ,0)  fail_qty_rate\n" +
        "FROM\n" +
        "	mes_record_work mrw\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = mrw.machine_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_no = mrw.part_no\n" +
        "LEFT JOIN base_mold bmld ON bmld.mold_id = mrw.mold_id\n" +
        "LEFT JOIN mes_mo_schedule_station mmss ON mmss.schedule_id = mrw.schedule_id\n" +
        "AND mmss.station_id = mrw.station_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON mms.schedule_id = mrw.schedule_id\n" +
        "LEFT JOIN iot_machine_output imo ON imo.org_id = bm.id\n" +
        "LEFT JOIN mes_record_staff mrs ON mrs.rw_id = mrw.rwid\n" +
        "LEFT JOIN mes_mo_schedule_staff mmsff ON mrw.schedule_id = mmsff.schedule_id\n" +
        "AND mmsff.staff_id = mrs.staff_id\n" +
        "LEFT JOIN base_shift bs ON bs.shift_id = mmsff.shift_id\n" +
        "LEFT JOIN base_station bstion  on bstion.station_id=mrw.station_id and ( bstion.`code` in ('KZ001','KZ001-1')  )\n" +
        "WHERE\n" +
        "	bstion.station_id  is NOT NULL \n" ;
        if(machineProduceQuery.getMachineids()!=null&& machineProduceQuery.getMachineids().length>0){
          String machneids = Arrays.stream(machineProduceQuery.getMachineids()).collect(Collectors.joining("','", "'", "'"));
          sql +=  "  and  mrw.machine_id in("+machneids+")\n";
        }
        if (StringUtils.isNotEmpty(machineProduceQuery.getShiftId())) {
              sql +="    and  mmsff.shift_id='"+machineProduceQuery.getShiftId()+"'\n" ;
        }
        if(machineProduceQuery.getStartTime()!=null){
              sql += "   and  mrw.start_time   LIKE '"+ DateUtil.format(machineProduceQuery.getStartTime(),DateUtil.DATE_PATTERN)+"%'  ";
        }
    RowMapper<MachineProduce> rowMapper = BeanPropertyRowMapper.newInstance(MachineProduce.class);
    List<MachineProduce> machineProduces = jdbcTemplate.query(sql, rowMapper).stream().filter(x->{
      x.setMachineTimeCost(DateUtil.getGapTime(Long.parseLong(x.getMachineTimeCost())));
      return true;
    }).collect(Collectors.toList());

    return machineProduces;
  }
}
