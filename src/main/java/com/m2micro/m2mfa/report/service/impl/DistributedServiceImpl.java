package com.m2micro.m2mfa.report.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.m2micro.m2mfa.common.util.ChinaFontProvide;
import com.m2micro.m2mfa.common.util.FileUtil;
import com.m2micro.m2mfa.common.util.POIReadExcelToHtml;
import com.m2micro.m2mfa.report.query.DistributedQuery;
import com.m2micro.m2mfa.report.service.DistributedService;
import com.m2micro.m2mfa.report.vo.Distributed;
import com.m2micro.m2mfa.report.vo.DistributedDate;
import com.m2micro.m2mfa.report.vo.MacheineAndOutputQty;
import com.m2micro.m2mfa.report.vo.ProcessAndOutputQty;
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

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DistributedServiceImpl implements DistributedService {


  @Autowired
  @Qualifier("secondaryJdbcTemplate")
  private JdbcTemplate jdbcTemplate;


  @Override
  public DistributedDate DistributedShow(DistributedQuery distributedQuery) {
    List<Distributed> distributeds = getDistributeds(distributedQuery);
    DistributedDate distributedDate = new DistributedDate();
    distributedDate.setDistributeds(distributeds);
    List<ProcessAndOutputQty> processAndOutputQties = getProcessAndOutputQties(distributedQuery);
    distributedDate.setProcessAndOutputQtys(processAndOutputQties);
    List<MacheineAndOutputQty> macheineAndOutputQties = getMacheineAndOutputQties(distributedQuery);
    distributedDate.setMacheineAndOutputQtys(macheineAndOutputQties);
    return distributedDate;
  }

  /**
   * 获取机台跟总产量
   * @param distributedQuery
   * @return
   */
  private List<MacheineAndOutputQty> getMacheineAndOutputQties(DistributedQuery distributedQuery) {
    String machinesql ="SELECT\n" +
        "	bm.`name` machine_name ,SUM(vmpi.output_qty)  output_qty\n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = vmpi.mo_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON vmpi.schedule_id = mms.schedule_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = mms.machine_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmpi.part_id\n" +
        "LEFT JOIN base_process bpd on bpd.process_id=vmpi.process_id\n" +
        "WHERE\n" +
        "	mmd.close_flag = 3\n" ;
    if(StringUtils.isNotEmpty(distributedQuery.getPartNo())){
      machinesql += "   and bp.part_no='"+distributedQuery.getPartNo()+"'";
    }
    if(StringUtils.isNotEmpty(distributedQuery.getMoNumber())){
      machinesql += "   and mmd.mo_number ='"+distributedQuery.getMoNumber()+"'";
    }
    machinesql += " GROUP BY  bm.`name`";
    RowMapper<MacheineAndOutputQty> rowMapper = BeanPropertyRowMapper.newInstance(MacheineAndOutputQty.class);
    return jdbcTemplate.query(machinesql, rowMapper);
  }


  /**
   * 工序的总排产数量
   * @param distributedQuery
   * @return
   */
  private List<ProcessAndOutputQty> getProcessAndOutputQties(DistributedQuery distributedQuery) {
    String processsSql ="SELECT\n" +
        "	 bpd.process_name,SUM(vmpi.output_qty)  output_qty\n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = vmpi.mo_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON vmpi.schedule_id = mms.schedule_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = mms.machine_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmpi.part_id\n" +
        "LEFT JOIN base_process bpd on bpd.process_id=vmpi.process_id\n" +
        "WHERE\n" +
        "	mmd.close_flag = 3\n" ;
    if(StringUtils.isNotEmpty(distributedQuery.getPartNo())){
      processsSql += "   and bp.part_no='"+distributedQuery.getPartNo()+"'";
    }
    if(StringUtils.isNotEmpty(distributedQuery.getMoNumber())){
      processsSql += "   and mmd.mo_number ='"+distributedQuery.getMoNumber()+"'";
    }
    processsSql+= " GROUP BY  bpd.process_name;";
    RowMapper<ProcessAndOutputQty> rowMapper = BeanPropertyRowMapper.newInstance(ProcessAndOutputQty.class);
    return jdbcTemplate.query(processsSql, rowMapper);
  }

  /**
   * 获取详情数据
   * @param distributedQuery
   * @return
   */
  private List<Distributed> getDistributeds(DistributedQuery distributedQuery) {
    String sql="SELECT\n" +
        "bp.part_no,\n" +
        "	bp.`name` part_name,\n" +
        "	bp.spec,\n" +
        "	mmd.mo_number,\n" +
        "	mms.schedule_no,\n" +
        "	bpd.process_name,\n" +
        "	vmpi.output_qty,\n" +
        "	bm.`name` machine_name\n" +
        "	\n" +
        "FROM\n" +
        "	v_mes_process_info vmpi\n" +
        "LEFT JOIN mes_mo_desc mmd ON mmd.mo_id = vmpi.mo_id\n" +
        "LEFT JOIN mes_mo_schedule mms ON vmpi.schedule_id = mms.schedule_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = mms.machine_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmpi.part_id\n" +
        "LEFT JOIN base_process bpd on bpd.process_id=vmpi.process_id\n"+
        "WHERE\n" +
        "	mmd.close_flag = 3\n" ;
    if(StringUtils.isNotEmpty(distributedQuery.getPartNo())){
      sql += "   and bp.part_no='"+distributedQuery.getPartNo()+"'";
    }
    if(StringUtils.isNotEmpty(distributedQuery.getMoNumber())){
      sql += "   and mmd.mo_number ='"+distributedQuery.getMoNumber()+"'";
    }
    sql +="ORDER BY\n" +
        "	bp.part_id , mms.schedule_no";

    RowMapper<Distributed> rowMapper = BeanPropertyRowMapper.newInstance(Distributed.class);

    return jdbcTemplate.query(sql, rowMapper);
  }


  @Override
  public void excelOutData(DistributedQuery distributedQuery, HttpServletResponse response) throws Exception {
    DistributedDate distributedDate = DistributedShow(distributedQuery);
    List<Distributed> distributeds=distributedDate.getDistributeds();
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("在制报表");
    List<Integer> integers = GroupBy(distributedQuery);
    Integer y=1;
    System.out.println("xxxxx"+integers.size());
    for( int x =0; x<integers.size();x++){
      Integer i = integers.get(x);
      int v=y+i;
      v = v - 1;
      System.out.println("y==="+y+"v===="+v+"x==="+x);
      sheet1.addMergedRegion(new CellRangeAddress(y,v,0,0));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,1,1));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,2,2));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,3,3));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,4,4));
      y+=i;

    }
    Row row =sheet1.createRow(0);
    addRowOne(row,book);
    addRowData(sheet1,distributeds,book);
    book.close();
    FileUtil.excelDownloadFlie(response, book,"distributed");

  }

  @Override
  public void pdfOutData(DistributedQuery distributedQuery, HttpServletResponse response)throws Exception  {
    DistributedDate distributedDate = DistributedShow(distributedQuery);
    List<Distributed> distributeds=distributedDate.getDistributeds();
    Workbook book = new HSSFWorkbook();
    // 在对应的Excel中建立一个分表
    Sheet sheet1 = book.createSheet("在制报表");
    List<Integer> integers = GroupBy(distributedQuery);
    Integer y=1;
    System.out.println("xxxxx"+integers.size());
    for( int x =0; x<integers.size();x++){
      Integer i = integers.get(x);
      int v=y+i;
      v = v - 1;
      System.out.println("y==="+y+"v===="+v+"x==="+x);
      sheet1.addMergedRegion(new CellRangeAddress(y,v,0,0));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,1,1));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,2,2));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,3,3));
      sheet1.addMergedRegion(new CellRangeAddress(y,v,4,4));
      y+=i;

    }
    Row row =sheet1.createRow(0);
    addRowOne(row,book);
    addRowData(sheet1,distributeds,book);
    // 保存到计算机相应路径
    String fileSeperator = File.separator;
    book.write( new FileOutputStream(fileSeperator+"distributed.xls"));
    book.close();
    String html =  POIReadExcelToHtml.readExcelToHtml(fileSeperator+"distributed.xls", true);
    try {
      Document document = new Document();
      PdfWriter mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(new File(fileSeperator+"distributed.pdf")));
      document.open();
      ByteArrayInputStream bin = new ByteArrayInputStream(html.getBytes());
      XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, null, new ChinaFontProvide());
      System.out.println("生成完毕"+fileSeperator);
      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    FileUtil.download(fileSeperator+"distributed.pdf",response,false,"distributed.pdf");
  }

  /**
   * N   行数据
   * @param sheet1
   */
  public void addRowData(Sheet sheet1, List<Distributed> distributeds, Workbook book){
    Set<String> ids = new HashSet<>();
    for(int i =0; i<distributeds.size();i++){
      Row row =sheet1.createRow(i+1);
      sheet1.setDefaultRowHeightInPoints(20);
      sheet1.setDefaultColumnWidth(20);
      getRow(row,distributeds.get(i),book,ids);
    }
  }

  private void getRow( Row row,Distributed distributed,Workbook book, Set<String> ids) {
    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for(int i=0;i<9; i++){
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      ids.add(distributed.getPartNo());
      switch (i){
        case 0:
          cell.setCellValue(ids.size());
          break;
        case 1:
          cell.setCellValue(distributed.getPartNo());
          break;
        case 2:
          cell.setCellValue(distributed.getPartName());
          break;
        case 3:
          cell.setCellValue(distributed.getSpec());
          break;
        case 4:
          cell.setCellValue(distributed.getMoNumber());
          break;
        case 5:
          cell.setCellValue(distributed.getMachineName());
          break;
        case 6:
          cell.setCellValue(distributed.getScheduleNo());
          break;
        case 7:
          cell.setCellValue(distributed.getProcessName());
          break;
        case 8:
          cell.setCellValue(distributed.getOutputQty());
          break;
      }
    }
  }


  /**
   * 第一行标题
   * @param row
   */
  public void addRowOne( Row row,Workbook book) {
    HSSFCellStyle hssfCellStyle = getHssfCellStyle(book);
    for (int i = 0; i < 9; i++) {
      Cell cell = row.createCell(i);
      cell.setCellStyle(hssfCellStyle);
      switch (i) {
        case 0:
          cell.setCellValue("序号");
          break;
        case 1:
          cell.setCellValue("料件编码");
          break;
        case 2:
          cell.setCellValue("料件名称");
          break;
        case 3:
          cell.setCellValue("规格");
          break;
        case 4:
          cell.setCellValue("工单号码");
          break;
        case 5:
          cell.setCellValue("作业机台");
          break;
        case 6:
          cell.setCellValue("排产单编号");
          break;
        case 7:
          cell.setCellValue("工序名称");
          break;
        case 8:
          cell.setCellValue("在制数量");
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
     * 获取分组条数
     * @param distributedQuery
     * @return
     */
  public List<Integer> GroupBy(DistributedQuery distributedQuery){
    String sql ="SELECT\n" +
        "	COUNT(*)\n" +
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
     sql +=  "GROUP BY\n" +
        "	bp.part_no";

    List<Integer> lists = jdbcTemplate.queryForList(sql, Integer.class);
    return  lists;
  }
}
