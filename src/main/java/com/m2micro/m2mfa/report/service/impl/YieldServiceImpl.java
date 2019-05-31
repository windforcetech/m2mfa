package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.kanban.vo.MachinerealTimeStatus;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

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
    List<Yield> collect = jdbcTemplate.query(sql, rowMapper).stream().filter(x -> {
      x.setCloseFlag(MoStatus.valueOf(Integer.parseInt(x.getCloseFlag())).getValue());
      return true;
    }).collect(Collectors.toList());

    return collect ;
  }

  @Override
  public void excelOutData(YieldQuery yieldQuery)throws Exception {
    List<Yield> yields = YieldShow(yieldQuery);
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("产量报表");
    List<Integer> integers = GroupBy(yieldQuery);
    Integer y=1;
    for( int x =0; x<integers.size();x++){
      Integer i = integers.get(x);
      int v=y+i;
      sheet1.addMergedRegion(new CellRangeAddress(y,v,0,0));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,1,1));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,2,2));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,3,3));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,4,4));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,5,5));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,6,6));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,7,7));
      y+=i;
    }

    Row row =sheet1.createRow(0);
    addRowOne(row,book);
    addRowData(sheet1,yields,book);
    // 保存到计算机相应路径
    book.write( new FileOutputStream("D://a.xls"));
  }


  /**
   * N   行数据
   * @param sheet1
   */
  public void addRowData( Sheet sheet1,List<Yield> yields,Workbook book){
    for(int i =0; i<yields.size();i++){
      Row row =sheet1.createRow(i+1);
      sheet1.setDefaultRowHeightInPoints(20);
      sheet1.setDefaultColumnWidth(20);
      getRow(row,yields.get(i),i+1,book);
    }
  }

  private void getRow( Row row,Yield yield,Integer id,Workbook book) {
    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<10; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){
        case 0:
          cell.setCellValue(id);
          break;
        case 1:
          cell.setCellValue(yield.getMoNumber());
          break;
        case 2:
          cell.setCellValue(yield.getCloseFlag());
          break;
        case 3:
          cell.setCellValue(yield.getPartNo());
          break;
        case 4:
          cell.setCellValue(yield.getPartName());
          break;
        case 5:
          cell.setCellValue(yield.getSpec());
          break;
        case 6:
          cell.setCellValue(yield.getTargetQty());
          break;
        case 7:
          cell.setCellValue(yield.getProductionUnit());
          break;
        case 8:
          cell.setCellValue(yield.getProcessName());
          break;
        case 9:
          cell.setCellValue(yield.getOutputQty());
          break;
        case 10:
          cell.setCellValue(yield.getFailQty());
          break;
      }
    }
  }

  /**
   * 设置文字居中
   * @param book
   * @return
   */
  private  HSSFCellStyle getHssfCellStyle(Workbook book) {
    HSSFCellStyle style2 = (HSSFCellStyle) book.createCellStyle();
    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style2;
  }
  /**
   * 第一行标题
   * @param row
   */
  public void addRowOne( Row row,Workbook book){
    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<10; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){
        case 0:
          cell.setCellValue("序号");
          break;
        case 1:
          cell.setCellValue("工单号码");
          break;
        case 2:
          cell.setCellValue("工单状态");
          break;
        case 3:
          cell.setCellValue("物料编号");
          break;
        case 4:
          cell.setCellValue("物料名称");
          break;
        case 5:
          cell.setCellValue("规格");
          break;
        case 6:
          cell.setCellValue("计划数");
          break;
        case 7:
          cell.setCellValue("单位");
          break;
        case 8:
          cell.setCellValue("工序名称");
          break;
        case 9:
          cell.setCellValue("完成数量");
          break;
        case 10:
          cell.setCellValue("不良数量");
          break;
      }
    }

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


  /**
   * 获取每组几条数据进行导出
   * @param yieldQuery
   * @return
   */
  public List<Integer> GroupBy(YieldQuery yieldQuery) {
    String sql ="SELECT  count(*)"+
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
    sql += " GROUP BY\n" +
        "	mmd.mo_number";
    List<Integer> lists = jdbcTemplate.queryForList(sql, Integer.class);
    return lists ;
  }

}
