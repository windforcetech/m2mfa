package com.m2micro.m2mfa.report.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.m2micro.framework.commons.util.PageUtil;
import com.m2micro.m2mfa.common.util.ChinaFontProvide;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.FileUtil;
import com.m2micro.m2mfa.common.util.POIReadExcelToHtml;
import com.m2micro.m2mfa.mo.constant.MoStatus;
import com.m2micro.m2mfa.report.query.YieldDataQuery;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class YieldServiceImpl implements YieldService {

  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Yield> yieldShow(YieldQuery yieldQuery) {
    String sql = getBaoBiaSql();
    sql += pingSql(yieldQuery);
    RowMapper<Yield> rowMapper = BeanPropertyRowMapper.newInstance(Yield.class);
    List<Yield> collect = jdbcTemplate.query(sql, rowMapper).stream().filter(x -> {
      x.setCloseFlag(MoStatus.valueOf(Integer.parseInt(x.getCloseFlag())).getValue());
      return true;
    }).collect(Collectors.toList());

    return collect ;
  }

  @Override
  public PageUtil<Yield> yielddata(YieldDataQuery yieldQuery) {
    YieldQuery query = new YieldQuery();
    BeanUtils.copyProperties(yieldQuery, query);
    String sql = getBaoBiaSql();
    sql += pingSql(query);
    sql +=  " LIMIT   " + (yieldQuery.getPage() - 1) * yieldQuery.getSize() + "," + yieldQuery.getSize();
    RowMapper<Yield> rowMapper = BeanPropertyRowMapper.newInstance(Yield.class);
    List<Yield> collect = jdbcTemplate.query(sql, rowMapper).stream().filter(x -> {
      x.setCloseFlag(MoStatus.valueOf(Integer.parseInt(x.getCloseFlag())).getValue());
      return true;
    }).collect(Collectors.toList());
    return PageUtil.of(collect, getcount(query), yieldQuery.getSize(), yieldQuery.getPage());
  }

  /**
   * 报表，跟显示共用sql
   * @return
   */
  private String getBaoBiaSql() {
    return "SELECT\n" +
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
        "			)  AND mmsp.process_id=bp.process_id \n" +
        "	) output_qty,\n" +
        "(\n" +
        "		SELECT\n" +
        "			IFNULL(SUM(fail_qty), 0) failsum\n" +
        "		FROM\n" +
        "			mes_record_wip_fail mrw\n" +
        "		WHERE\n" +
        "			mrw.mo_id = mmd.mo_id\n" +
        "		AND mrw.target_process_id = bp.process_id\n" +
        "	) fail_qty\n" +
        "FROM\n";

  }

  /**
   * 获取总页数
   * @param yieldQuery
   * @return
   */
  public long getcount(YieldQuery yieldQuery) {
    String sql ="SELECT  count(*)  from " ;
    sql += pingSql(yieldQuery);
    return jdbcTemplate.queryForObject(sql, Long.class);
  }

  /**
   * 共用代码
   * @param yieldQuery
   * @return
   */
  private String pingSql(YieldQuery yieldQuery) {
    String  sql="";

    sql+=   "	mes_mo_desc mmd\n" +
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
      sql += " and mmd.create_on='"+ DateUtil.format(yieldQuery.getProduceTime())+"'\n" ;
    }
    if (yieldQuery.getYieldStartTime() !=null &&  yieldQuery.getYieldEndTime()!=null) {
      sql += " and  mmd.create_on  >= '"+DateUtil.format(yieldQuery.getYieldStartTime())+"'     \n" ;
      sql += " and  mmd.create_on  <= '"+DateUtil.format(yieldQuery.getYieldEndTime())+"'     \n" ;
    }
    sql = isMoAndprossceAndChedule(yieldQuery, sql);
    sql += " ORDER BY\n" +
        "	mmd.mo_number ,mprp.Setp  ";
    return sql;
  }


  /**
   * 工单完成，排工完成，工序完成
   * @param yieldQuery
   * @param sql
   * @return
   */
  private String isMoAndprossceAndChedule(YieldQuery yieldQuery, String sql) {
    if (yieldQuery.isMoflag()) {
      sql += " and (mmd.close_flag="+MoStatus.CLOSE.getKey()+" or mmd.close_flag="+MoStatus.FORCECLOSE.getKey()+")";
    }
    if (yieldQuery.isProcessflag()) {
      sql += "  and (\n" +
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
          "	)>= mmd.target_qty";
    }
    if (yieldQuery.isCheduleflag()) {
      sql += " and mmd.is_schedul=true";
    }
    return sql;
  }


  @Override
  public void excelOutData(YieldQuery yieldQuery, HttpServletResponse response)throws Exception {

    List<Yield> yields = yieldShow(yieldQuery);
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("产量报表");
    getCombination(yieldQuery, sheet1);
    Row row =sheet1.createRow(0);
    addRowOne(row,book);
    addRowData(sheet1,yields,book);
    book.close();
    FileUtil.excelDownloadFlie(response, book,"yield");
  }

  @Override
  public void pdfOutData(YieldQuery yieldQuery, HttpServletResponse response) throws Exception {
    List<Yield> yields = yieldShow(yieldQuery);
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("产量报表");
    getCombination(yieldQuery, sheet1);
    Row row =sheet1.createRow(0);
    addRowOne(row,book);
    addRowData(sheet1,yields,book);
    // 保存到计算机相应路径
    String fileSeperator = File.separator;
    book.write( new FileOutputStream(fileSeperator+"yield.xls"));
    book.close();
    String html =  POIReadExcelToHtml.readExcelToHtml(fileSeperator+"yield.xls", true);
    try {
      Document document = new Document();
      PdfWriter mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(new File(fileSeperator+"yield.pdf")));
      document.open();
      ByteArrayInputStream bin = new ByteArrayInputStream(html.getBytes());
      XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, null, new ChinaFontProvide());

      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    FileUtil.download(fileSeperator+"yield.pdf",response,false,"yield.pdf");
  }

  private void getCombination(YieldQuery yieldQuery, Sheet sheet1) {
    List<Integer> integers = GroupBy(yieldQuery);
    Integer y = 1;
    for (int x = 0; x < integers.size(); x++) {
      Integer i = integers.get(x);
      int v = y + i;
      v = v - 1;
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 0, 0));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 1, 1));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 2, 2));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 3, 3));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 4, 4));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 5, 5));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 6, 6));
      sheet1.addMergedRegion(new CellRangeAddress(y, v, 7, 7));
      y += i;

    }
  }


  /**
   * N   行数据
   * @param sheet1
   */
  public void addRowData( Sheet sheet1,List<Yield> yields,Workbook book){
    Set<String> ids = new HashSet<>();
    for(int i =0; i<yields.size();i++){
      Row row =sheet1.createRow(i+1);
      sheet1.setDefaultRowHeightInPoints(20);
      sheet1.setDefaultColumnWidth(20);
      getRow(row,yields.get(i),book,ids);
    }
  }


  private void getRow( Row row,Yield yield, Workbook book, Set<String> ids ) {


    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<11; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      ids.add(yield.getMoNumber());
      switch (i){
        case 0:
          cell.setCellValue(ids.size());
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
    for(int i=0;i<11; i++){
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
    if (yieldQuery.getYieldStartTime() !=null &&  yieldQuery.getYieldEndTime()!=null) {
      // sql = getTimeCondition(yieldQuery.getTimecondition(), sql);
      sql += " and  mmd.create_on  >= '"+DateUtil.format(yieldQuery.getYieldStartTime())+"'     \n" ;
      sql += " and  mmd.create_on  <= '"+DateUtil.format(yieldQuery.getYieldEndTime())+"'     \n" ;
    }
    sql += " GROUP BY\n" +
        "	mmd.mo_number";
    List<Integer> lists = jdbcTemplate.queryForList(sql, Integer.class);
    return lists ;
  }


}
