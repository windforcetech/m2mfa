package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeStatus;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class YieldServiceImpl implements YieldService {

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Yield> YieldShow(YieldQuery yieldQuery) {
    String sql ="SELECT\n" +
        "	mmd.mo_number,\n" +
        "	mmd.close_flag,\n" +
        "	bpi.part_no partNo,\n" +
        "	bpi.`name` partName,\n" +
        "	bpi.spec,\n" +
        "	mmd.target_qty,\n" +
        "	bpi.production_unit,\n" +
        "	bp.process_name,\n" +
        "	(\n" +
        "		SELECT\n" +
        "			IFNULL(SUM(mmsp.output_qty), 0)\n" +
        "		FROM\n" +
        "			mes_mo_schedule_process mmsp\n" +
        "		WHERE\n" +
        "			mmsp.schedule_id IN (\n" +
        "				SELECT\n" +
        "					mms.schedule_id\n" +
        "				FROM\n" +
        "					mes_mo_schedule mms\n" +
        "				WHERE\n" +
        "					mms.mo_id = mmd.mo_id\n" +
        "			)\n" +
        "	) output_qty,\n" +
        "	(\n" +
        "		SELECT\n" +
        "			IFNULL(SUM(mrwf.fail_qty), 0)\n" +
        "		FROM\n" +
        "			mes_record_wip_fail mrwf\n" +
        "		WHERE\n" +
        "			mrwf.schedule_id IN (\n" +
        "				SELECT\n" +
        "					mms.schedule_id\n" +
        "				FROM\n" +
        "					mes_mo_schedule mms\n" +
        "				WHERE\n" +
        "					mms.mo_id = mmd.mo_id\n" +
        "			)\n" +
        "	) fail_qty \n" +
        "FROM\n" +
        "	mes_mo_desc mmd\n" +
        "LEFT JOIN base_parts bpi ON bpi.part_id = mmd.part_id\n" +
        "LEFT JOIN mes_part_route mpr ON mpr.part_id = mmd.part_id\n" +
        "LEFT JOIN mes_part_route_process mprp ON mpr.part_route_id = mprp.partrouteid\n" +
        "LEFT JOIN base_process  bp  on bp.process_id=mprp.processid\n" +
        "where 1=1\n" ;

    if (StringUtils.isNotEmpty(yieldQuery.getMoNumber())) {
     sql += " and mmd.mo_number  LIKE '%"+yieldQuery.getMoNumber()+"%'\n" ;
    }
    if (StringUtils.isNotEmpty(yieldQuery.getProcessName())) {
      sql +=" and bp.process_name LIKE '%"+yieldQuery.getProcessName()+"%'\n" ;
    }
    if (StringUtils.isNotEmpty(yieldQuery.getPartNo())) {
      sql +=" and bpi.part_no LIKE '%"+yieldQuery.getPartNo()+"%'\n" ;
    }
    if (StringUtils.isNotEmpty(yieldQuery.getCategory())) {
      sql += " and bpi.category='"+yieldQuery.getCategory()+"'\n" ;
    }

    if (yieldQuery.getProduceTime()!=null) {
      sql += " and mmd.create_on='"+DateUtil.format(yieldQuery.getProduceTime())+"'\n" ;
    }
    if (StringUtils.isNotEmpty(yieldQuery.getTimecondition())) {
      sql = getTimeCondition(yieldQuery.getTimecondition(), sql);
    }
    sql += " ORDER BY\n" +
        "	mmd.mo_number";
    RowMapper<Yield> rowMapper = BeanPropertyRowMapper.newInstance(Yield.class);
    return  jdbcTemplate.query(sql , rowMapper);
  }


  /**
   * 时间端获取
   * @param timecondition
   * @param sql
   * @return
   */
  private String getTimeCondition(String  timecondition, String sql) {
    switch (timecondition){
      case "month":
         sql += "  and DATE_FORMAT(mmd.create_on,'%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH),'%Y %m')\n" ;
        break;
      case "onmonth":
        sql +=  "  and DATE_FORMAT(mmd.create_on, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y %m')\n" ;
        break;
      case "day":
        sql +=  "   and to_days( mmd.create_on ) = to_days(now())\n" ;
        break;
      case "yesterday":
        sql += "   and  TO_DAYS( NOW( ) ) - TO_DAYS( mmd.create_on ) <= 1\n" ;
        break;
      case "week":
        sql += "  and YEARWEEK(date_format(mmd.create_on,'%Y-%m-%d')) = YEARWEEK(now())\n" ;
        break;
      case "onweek":
        sql += " and  YEARWEEK(date_format(mmd.create_on,'%Y-%m-%d')) = YEARWEEK(now())-1\n" ;
        break;
      case "year":
        sql +=  "  and YEAR(mmd.create_on)=YEAR(NOW())\n" ;
        break;
      case "onyear":
        sql += " and  year(mmd.create_on)=year(date_sub(now(),interval 1 year))\n" ;
        break;
      case "season":
        sql += "  and QUARTER(mmd.create_on)=QUARTER(now())\n" ;
        break;
      case "onseason":
        sql += " and  QUARTER(mmd.create_on)=QUARTER(DATE_SUB(now(),interval 1 QUARTER))\n" ;
        break;
    }
    return sql;
  }


}
