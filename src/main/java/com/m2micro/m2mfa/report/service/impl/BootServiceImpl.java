package com.m2micro.m2mfa.report.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.m2micro.m2mfa.common.util.ChinaFontProvide;
import com.m2micro.m2mfa.common.util.DateUtil;
import com.m2micro.m2mfa.common.util.FileUtil;
import com.m2micro.m2mfa.common.util.POIReadExcelToHtml;
import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.Boot;
import com.m2micro.m2mfa.report.vo.BootAndData;
import com.m2micro.m2mfa.report.vo.ShiftAndData;
import com.m2micro.m2mfa.report.vo.Yield;
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

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
      Long bad = getFailCount(bootQuery);
      bootAndData.setBoots(boots);
      bootAndData.setBad(bad);
      List<ShiftAndData> shiftAndData = getShiftAndData(bootQuery);
      bootAndData.setShiftAndDatas(shiftAndData);
    }
    return bootAndData;
  }


  @Override
  public void excelOutData(BootQuery bootQuery, HttpServletResponse response) throws Exception {

    BootAndData bootAndData = BootShow(bootQuery);
    if(bootAndData !=null){
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("开机报表");
    int shiftMun =bootAndData.getShiftAndDatas().size();
    sheet1.addMergedRegion(new CellRangeAddress(0, shiftMun, 0, 0));
    sheet1.addMergedRegion(new CellRangeAddress(0, shiftMun, 1, 1));
    for(int x =0; x<shiftMun+1; x++){
      sheet1.addMergedRegion(new CellRangeAddress(x, x, 2, 3));
      sheet1.addMergedRegion(new CellRangeAddress(x, x, 4, 5));
      sheet1.addMergedRegion(new CellRangeAddress(x, x, 10, 12));
      sheet1.addMergedRegion(new CellRangeAddress(x, x, 14, 15));
    }

    Row rowA =sheet1.createRow(0);
    addRowA(rowA,book,bootAndData);
    addRowShiftData(sheet1,bootAndData,book);
    Row row =sheet1.createRow(shiftMun+1);
    addRowOne(row,book);
    addRowData(sheet1,bootAndData,book);
    book.close();
    FileUtil.excelDownloadFlie(response, book,"boot");
    }
  }


  @Override
  public void pdfOutData(BootQuery bootQuery, HttpServletResponse response) throws Exception {

    BootAndData bootAndData = BootShow(bootQuery);
    if(bootAndData !=null) {
      Workbook book = new HSSFWorkbook();
      // 在对应的Excel中建立一个分表
      Sheet sheet1 = book.createSheet("开机报表");
      int shiftMun = bootAndData.getShiftAndDatas().size();
      sheet1.addMergedRegion(new CellRangeAddress(0, shiftMun, 0, 0));
      sheet1.addMergedRegion(new CellRangeAddress(0, shiftMun, 1, 1));
      for (int x = 0; x < shiftMun + 1; x++) {
        sheet1.addMergedRegion(new CellRangeAddress(x, x, 2, 3));
        sheet1.addMergedRegion(new CellRangeAddress(x, x, 4, 5));
        sheet1.addMergedRegion(new CellRangeAddress(x, x, 10, 12));
        sheet1.addMergedRegion(new CellRangeAddress(x, x, 14, 15));
      }

      Row rowA = sheet1.createRow(0);
      addRowA(rowA, book, bootAndData);
      addRowShiftData(sheet1, bootAndData, book);
      Row row = sheet1.createRow(shiftMun + 1);
      addRowOne(row, book);
      addRowData(sheet1, bootAndData, book);
      // 保存到计算机相应路径
      String fileSeperator = File.separator;
      book.write(new FileOutputStream(fileSeperator + "boot.xls"));
      book.close();
      String html = POIReadExcelToHtml.readExcelToHtml(fileSeperator + "boot.xls", true);
      try {
        Document document = new Document();
        PdfWriter mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(new File(fileSeperator + "boot.pdf")));
        document.open();
        ByteArrayInputStream bin = new ByteArrayInputStream(html.getBytes());
        XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, null, new ChinaFontProvide());

        document.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      FileUtil.download(fileSeperator + "boot.pdf", response, false, "boot.pdf");
    }

  }


  /**
   * N   行数据
   * @param sheet1
   */
  public void addRowData( Sheet sheet1 ,BootAndData bootAndData,Workbook book){
    for(int i =0; i<bootAndData.getBoots().size();i++){
      int x =i+1+bootAndData.getShiftAndDatas().size()+1;
      Row row =sheet1.createRow(x);
      sheet1.setDefaultRowHeightInPoints(20);
      sheet1.setDefaultColumnWidth(20);
      getRow(row,bootAndData.getBoots().get(i),book);
    }
  }


  /**
   * N   行数据
   * @param sheet1
   */
  public void addRowShiftData( Sheet sheet1 ,BootAndData bootAndData,Workbook book){
    for(int i =0; i<bootAndData.getShiftAndDatas().size();i++){
      int x =i+bootAndData.getShiftAndDatas().size()-1;
      if(x==0){
        x=x+1;
      }
      System.out.println("======"+x);
      Row row =sheet1.createRow(x);
      sheet1.setDefaultRowHeightInPoints(20);
      sheet1.setDefaultColumnWidth(20);
      getShiftRow(row,bootAndData.getShiftAndDatas().get(i),book);
    }
  }


  private void getShiftRow( Row row,ShiftAndData shiftAndData, Workbook book ) {

    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<17; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){

        case 2:
          cell.setCellValue(shiftAndData.getShiftName());
          break;
        case 4:
          cell.setCellValue(shiftAndData.getShiftSummary());
          break;
        case 6:
          cell.setCellValue(shiftAndData.getShiftName());
          break;
        case 7:
          cell.setCellValue(shiftAndData.getShiftAchievingRate());
          break;
        case 8:
          cell.setCellValue(shiftAndData.getShiftName());
          break;
        case 9:
          cell.setCellValue(shiftAndData.getShiftBad());
          break;
        case 10:
          cell.setCellValue(shiftAndData.getShiftName());
          break;
        case 13:
          cell.setCellValue(shiftAndData.getShiftHours());
          break;
        case 14:
          cell.setCellValue(shiftAndData.getShiftName());
          break;
        case 16:
          cell.setCellValue(shiftAndData.getShiftMean());
          break;

      }
    }
  }


  private void getRow( Row row,Boot boot, Workbook book ) {

    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<17; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){
        case 0:
          cell.setCellValue(DateUtil.format(boot.getStartTime(), DateUtil.DATE_PATTERN));
          break;
        case 1:
          cell.setCellValue(boot.getShiftName());
          break;
        case 2:
          cell.setCellValue(boot.getMachineName());
          break;
        case 3:
          cell.setCellValue(boot.getStaffCode());
          break;
        case 4:
          cell.setCellValue(boot.getStaffName());
          break;
        case 5:
          cell.setCellValue(boot.getUseTime());
          break;
        case 6:
          cell.setCellValue(boot.getMachineTime());

          break;
        case 7:
          cell.setCellValue(boot.getPartName());

          break;
        case 8:
          cell.setCellValue(boot.getMoldCode());

          break;
        case 9:
          cell.setCellValue(boot.getMolds());

          break;
        case 10:
          cell.setCellValue(boot.getCavityQty());

          break;
        case 11:
          cell.setCellValue(boot.getScheduleQty());

          break;
        case 12:
          cell.setCellValue(String.valueOf(boot.getReach()));

          break;
        case 13:
          cell.setCellValue(String.valueOf(boot.getOutputQty()));

          break;
        case 14:
          cell.setCellValue(String.valueOf(boot.getAchievingRate()));

          break;
        case 15:
          cell.setCellValue(String.valueOf(boot.getScrapQty()));

          break;
        case 16:
          cell.setCellValue(String.valueOf(boot.getFailQty()));

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
   * 总汇
   * @param row
   */
  public void addRowA( Row row,Workbook book,BootAndData bootAndData){

    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<17; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){
        case 0:
          cell.setCellValue("日期");
          break;
        case 1:
          cell.setCellValue(DateUtil.format(bootAndData.getStartTime(), DateUtil.DATE_PATTERN));
          break;
        case 2:
          cell.setCellValue("总生产数（PCS）");
          break;
        case 4:
          cell.setCellValue(bootAndData.getSummary());
          break;
        case 6:
          cell.setCellValue("达成率");
          break;
        case 7:
          cell.setCellValue(bootAndData.getAchievingRate());
          break;
        case 8:
          cell.setCellValue("不良");
          break;
        case 9:
          cell.setCellValue(bootAndData.getBad());
          break;
        case 10:
          cell.setCellValue("总人工工时（H）");
          break;
        case 13:
          cell.setCellValue(bootAndData.getHours());
          break;
        case 14:
          cell.setCellValue("人均产值（PCS）");
          break;
        case 16:
          cell.setCellValue(bootAndData.getMean());
          break;

      }
    }

  }


  /**
   * 第一行标题
   * @param row
   */
  public void addRowOne( Row row,Workbook book){

    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<17; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i){
        case 0:
          cell.setCellValue("归属日期");
          break;
        case 1:
          cell.setCellValue("班别");
          break;
        case 2:
          cell.setCellValue("机台");
          break;
        case 3:
          cell.setCellValue("工号");
          break;
        case 4:
          cell.setCellValue("姓名");
          break;
        case 5:
          cell.setCellValue("人力工时");
          break;
        case 6:
          cell.setCellValue("机台工时");
          break;
        case 7:
          cell.setCellValue("品名");
          break;
        case 8:
          cell.setCellValue("模号");
          break;
        case 9:
          cell.setCellValue("模数");
          break;
        case 10:
          cell.setCellValue("穴数");
          break;
        case 11:
          cell.setCellValue("目标");
          break;
        case 12:
          cell.setCellValue("达成数");
          break;
        case 13:
          cell.setCellValue("生产数");
          break;
        case 14:
          cell.setCellValue("达成率");
          break;
        case 15:
          cell.setCellValue("报废数");
          break;
        case 16:
          cell.setCellValue("不良率");
          break;
      }
    }

  }


  /**
   * 班别的分组数据
   * @return
   */
  private List<ShiftAndData> getShiftAndData(BootQuery bootQuery) {
    String sql="SELECT\n" +
        "	bs.`name` shift_name,DATE_FORMAT(mrw.start_time,'%Y-%m-%d') start_time,\n" +
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
        "(	"+tableName(bootQuery)+") vmsi\n" +
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
  private Long getFailCount(BootQuery bootQuery) {
    String sql ="select IFNULL(SUM(fail_qty),0)  from  mes_record_wip_fail where    create_on  LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'  and target_process_id IN(select process_id from base_process where process_code='gxdm')";
    return jdbcTemplate.queryForObject(sql, long.class);
  }


  /**
   * 获取汇总数据
   * @param bootQuery
   * @return
   */
  private BootAndData getBootAndData(BootQuery bootQuery) {
    String sql ="SELECT\n" +
        "	SUM(IFNULL(vmsi.output_qty, 0)) summary,\n" +
        "	IFNULL(\n" +
        "		CONVERT (\n" +
        "			SUM(vmsi.output_qty) / SUM(\n" +
        "				(\n" +
        "					CASE\n" +
        "					WHEN vmsi.end_time IS NULL THEN\n" +
        "						(\n" +
        "							UNIX_TIMESTAMP(vmsi.end_time) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "						) / vmsi.standard_hours\n" +
        "					ELSE\n" +
        "						(\n" +
        "							UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "						) / vmsi.standard_hours\n" +
        "					END\n" +
        "				)\n" +
        "			),\n" +
        "			DECIMAL (10, 2)\n" +
        "		),\n" +
        "		0.00\n" +
        "	) achieving_rate,\n" +
        "	SUM(\n" +
        "		(vmsi.end_time - vmsi.start_time)\n" +
        "	) use_time,\n" +
        "	SUM(IFNULL(vmsi.output_qty, 0)) / COUNT(*) mean\n" +
        "FROM  \n";
    sql +=pingSql(bootQuery);
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
    String sql ="	("+tableName(bootQuery )+" ) vmsi\n" +
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
        "		process_code = 'gxdm')\n";
      if(bootQuery.getBootTime()!=null){
       sql += "  and  vmsi.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'  ";
      }

    return  sql;
  }


  /**
   * 获取详情
   * @return
   */
  public  List<Boot>getBoots(BootQuery bootQuery){

   String sql= "SELECT\n" +
         "DATE_FORMAT(vmsi.start_time,'%Y-%m-%d') start_time	,\n" +
        "	bs.`name` shift_name,\n" +
        "	bm.`name` machine_name,\n" +
        "	bst.`code` staff_code,\n" +
        "	bst.staff_name,\n" +
        "	(\n" +
        "		IFNULL(vmsi.end_time,NOW()) - vmsi.start_time\n" +
        "	) use_time,\n" +
        "	(\n" +
        "		IFNULL(vmsi.end_time ,NOW())- vmsi.start_time\n" +
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
    sql +="  GROUP BY  vmsi.staff_id ";
    RowMapper<Boot> rowMapper = BeanPropertyRowMapper.newInstance(Boot.class);
     return  jdbcTemplate.query(sql, rowMapper);
  }


  /**
   * 获取组合表
   * @return
   */
  public String tableName(BootQuery bootQuery){
    String sql ="SELECT\n" +
        "	vm.*\n" +
        "FROM\n" +
        "	v_mes_staff_info vm  where  vm.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%' \n" +
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
        "		mes_staff_info msi  where  msi.start_time LIKE '"+ DateUtil.format(bootQuery.getBootTime(),DateUtil.DATE_PATTERN)+"%'";
    return  sql;

  }


}
