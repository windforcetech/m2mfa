package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.report.query.DistributedQuery;
import com.m2micro.m2mfa.report.service.DistributedService;
import com.m2micro.m2mfa.report.vo.Distributed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributedServiceImpl implements DistributedService {


  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;


  @Override
  public List<Distributed> DistributedShow(DistributedQuery distributedQuery) {

    String sql="SELECT\n" +
        "bp.part_no,\n" +
        "	bp.`name` part_name,\n" +
        "	bp.spec,\n" +
        "	mmd.mo_number,\n" +
        "	mms.schedule_no,\n" +
        "	vmpi.process_name,\n" +
        "	vmpi.output_qty,\n" +
        "	bm.`name` machine_name\n" +
        "	\n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = vmpi.mo_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON vmpi.schedule_id = mms.schedule_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = mms.machine_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmpi.part_id\n" +
        "WHERE\n" +
        "	mmd.close_flag = 3\n" ;
    if(StringUtils.isNotEmpty(distributedQuery.getPartNo())){
      sql += "   and bp.part_no='"+distributedQuery.getPartNo()+"'";
    }
    if(StringUtils.isNotEmpty(distributedQuery.getMoNumber())){
      sql += "   and mmd.mo_number ='"+distributedQuery.getMoNumber()+"'";
    }
    sql +="ORDER BY\n" +
        "	bp.part_id";


    RowMapper<Distributed> rowMapper = BeanPropertyRowMapper.newInstance(Distributed.class);
    List<Distributed> distributeds = jdbcTemplate.query(sql, rowMapper);
    return distributeds;
  }
}
