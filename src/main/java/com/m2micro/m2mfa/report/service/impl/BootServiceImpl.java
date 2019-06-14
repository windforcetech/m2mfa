package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.Boot;
import com.m2micro.m2mfa.report.vo.BootAndData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BootServiceImpl  implements BootService {

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;


  @Override
  public List<BootAndData> BootShow(BootQuery bootQuery) {

    String  sql= "SELECT\n" +
        "	vmsi.start_time,\n" +
        "	bs.`name` shift_name,\n" +
        "	bm.`name` machine_name,\n" +
        "	bst.`code` staff_code,\n" +
        "	bst.staff_name,\n" +
        "	(\n" +
        "		IFNULL(vmsi.end_time,NOW()) - vmsi.start_time\n" +
        "	) use_time,\n" +
        "	(\n" +
        "		IFNULL(mrw.end_time ,NOW())- mrw.start_time\n" +
        "	) machine_time,\n" +
        "  bp.`name` part_name,\n" +
        "	bmd.`code` mold_code,\n" +
        "	IFNULL(vmsi.molds,0) molds,\n" +
        "	bmd.cavity_qty,\n" +
        "	mms.schedule_qty,\n" +
        "	IFNULL( CONVERT (\n" +
        "		(\n" +
        "			CASE\n" +
        "			WHEN vmsi.end_time IS NULL THEN\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(vmsi.end_time) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			ELSE\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			END\n" +
        "		),\n" +
        "		DECIMAL (10, 2)\n" +
        "	),0.00) reach,\n" +
        "	IFNULL(vmsi.output_qty,0) output_qty,\n" +
        "	IFNULL( CONVERT (\n" +
        "		vmsi.output_qty / (\n" +
        "			CASE\n" +
        "			WHEN vmsi.end_time IS NULL THEN\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(vmsi.end_time) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			ELSE\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			END\n" +
        "		),\n" +
        "		DECIMAL (10, 2)\n" +
        "	),0.00) achieving_rate,\n" +
        "	vmsi.scrap_qty,\n" +
        "	vmsi.fail_qty\n" +
        "FROM";

    sql += pingSql(bootQuery);
    RowMapper<Boot> rowMapper = BeanPropertyRowMapper.newInstance(Boot.class);
    List<Boot> boots = jdbcTemplate.query(sql, rowMapper);

     sql ="SUM(IFNULL(vmsi.output_qty,0))  ";
     sql += pingSql(bootQuery);
     Long  summary = jdbcTemplate.queryForObject(sql, Long.class);



    return null;
  }


  /**
   * 共用代码片段
   * @param bootQuery
   * @return
   */
  public String pingSql(BootQuery bootQuery){
    String sql ="	v_mes_staff_info vmsi\n" +
        "LEFT JOIN mes_record_work mrw ON mrw.schedule_id = vmsi.schedule_id\n" +
        "AND mrw.process_id = vmsi.process_id\n" +
        "AND mrw.machine_id = vmsi.machine_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON mms.schedule_id = vmsi.schedule_id\n" +
        "LEFT JOIN base_mold bmd ON bmd.mold_id = vmsi.mold_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmsi.part_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = vmsi.machine_id\n" +
        "LEFT JOIN base_staff bst ON bst.staff_id = vmsi.staff_id\n" +
        "LEFT JOIN base_shift bs ON bs.shift_id = (\n" +
        "	SELECT\n" +
        "		shift_id\n" +
        "	FROM\n" +
        "		mes_mo_schedule_staff\n" +
        "	WHERE\n" +
        "		schedule_id = vmsi.schedule_id\n" +
        "	AND staff_id = vmsi.staff_id\n" +
        "	LIMIT 0,\n" +
        "	1\n" +
        ") where 1=1  ";
      if(bootQuery.getBootTime()!=null){
       sql += "   mrw.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'   #GROUP BY bs.shift_id";
      }

    return  sql;
  }


}
