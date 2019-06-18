package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.Boot;
import com.m2micro.m2mfa.report.vo.BootAndData;
import com.m2micro.m2mfa.report.vo.ShiftAndData;
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
  public BootAndData  BootShow(BootQuery bootQuery) {
    List<Boot> boots = getBoots(bootQuery);
    BootAndData bootAndData = getBootAndData(bootQuery);
    if(bootAndData !=null){
      Long bad = getFailCount();
      bootAndData.setBoots(boots);
      bootAndData.setBad(bad);
      List<ShiftAndData> shiftAndData = getShiftAndData(bootQuery);
      bootAndData.setShiftAndDatas(shiftAndData);
    }
    return bootAndData;
  }


  /**
   * 班别的分组数据
   * @return
   */
  private List<ShiftAndData> getShiftAndData(BootQuery bootQuery) {
    String sql="SELECT\n" +
        "	bs.`name` shift_name,\n" +
        "	vmsi.output_qty shift_summary,\n" +
        "	CONVERT (\n" +
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
        "	) achieving_rate,\n" +
        "	vmsi.fail_qty,\n" +
        "	(\n" +
        "		vmsi.end_time - vmsi.start_time\n" +
        "	) use_time,\n" +
        "	(\n" +
        "		vmsi.end_time - vmsi.start_time\n" +
        "	) / COUNT(*) shift_mean\n" +
        "FROM\n" +
        "(	"+tableName()+") vmsi\n" +
        "LEFT JOIN mes_record_work mrw ON mrw.schedule_id = vmsi.schedule_id\n"+
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
        ")\n" +
        "WHERE\n" +
        "	1 = 1\n" +
        "AND vmsi.process_id IN (\n" +
        "	SELECT\n" +
        "		process_id\n" +
        "	FROM\n" +
        "		base_process\n" +
        "	WHERE\n" +
        "		process_code = 'gxdm'\n" +
        ")\n" ;

    if(bootQuery.getBootTime()!=null){
      sql += "  and  mrw.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'  ";
    }
    sql += "  GROUP BY\n" +
        "	bs.shift_id";
    RowMapper<ShiftAndData> rowMapper = BeanPropertyRowMapper.newInstance(ShiftAndData.class);
    return jdbcTemplate.query(sql, rowMapper);
  }


  /**
   * 获取开机当前的不良总数
   * @return
   */
  private Long getFailCount() {
    String sql ="select  SUM(fail_qty) from  mes_record_wip_fail where    create_on LIKE'2019-06-14 %' and target_process_id IN(select process_id from base_process where process_code='gxdm')";
    return jdbcTemplate.queryForObject(sql, long.class);
  }


  /**
   * 获取汇总数据
   * @param bootQuery
   * @return
   */
  private BootAndData getBootAndData(BootQuery bootQuery) {
    String sql ="select SUM(IFNULL(vmsi.output_qty,0)) summary,	IFNULL( CONVERT (\n" +
         "		 SUM(vmsi.output_qty) / SUM( (\n" +
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
         "		)),\n" +
         "		DECIMAL (10, 2)\n" +
         "	),0.00) achieving_rate ,\n" +
         "  SUM( \n" +
         "	(\n" +
         "		vmsi.end_time - vmsi.start_time\n" +
         "	) \n" +
         " ) use_time\n" +
         ", (SUM( \n" +
         "	(\n" +
         "		vmsi.end_time - vmsi.start_time\n" +
         "	) \n" +
         " ))/COUNT(*)  mean from ";

    sql += pingSql(bootQuery);
    RowMapper<BootAndData> rowRoot = BeanPropertyRowMapper.newInstance(BootAndData.class);
    try {
      return jdbcTemplate.queryForObject(sql, rowRoot);
    }catch (Exception e){
      return null;
    }

  }


  /**
   * 共用代码片段
   * @param bootQuery
   * @return
   */
  public String pingSql(BootQuery bootQuery){
    String sql ="	("+tableName()+" ) vmsi\n" +
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
        ") where 1=1 and vmsi.process_id IN(select process_id from base_process where process_code='gxdm')   ";
      if(bootQuery.getBootTime()!=null){
       sql += "  and  mrw.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'  ";
      }

    return  sql;
  }

  /**
   * 获取详情
   * @return
   */
  public  List<Boot>getBoots(BootQuery bootQuery){

   String sql= "SELECT\n" +
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
     return  jdbcTemplate.query(sql, rowMapper);
  }

  /**
   * 获取组合表
   * @return
   */
  public String tableName(){
    String sql ="SELECT\n" +
        "	vm.*\n" +
        "FROM\n" +
        "	v_mes_staff_info vm\n" +
        "UNION\n" +
        "	SELECT\n" +
        "		msi.machine_id,\n" +
        "		msi.staff_id,\n" +
        "		msi.mo_id,\n" +
        "		msi.schedule_id,\n" +
        "		msi.process_id,\n" +
        "		msi.part_id,\n" +
        "		msi.start_time,\n" +
        "		msi.end_time,\n" +
        "		msi.flag,\n" +
        "		msi.output_qty,\n" +
        "		msi.fail_qty,\n" +
        "		msi.scrap_qty,\n" +
        "		msi.mold_id,\n" +
        "		msi.molds,\n" +
        "		msi.cavity_available,\n" +
        "		msi.standard_hours\n" +
        "	FROM\n" +
        "		mes_staff_info msi";
    return  sql;

  }
}


