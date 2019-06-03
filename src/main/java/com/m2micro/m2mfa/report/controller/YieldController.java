package com.m2micro.m2mfa.report.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.m2micro.framework.commons.annotation.UserOperationLog;
import com.m2micro.framework.commons.model.ResponseMessage;
import com.m2micro.m2mfa.common.util.PDFKit;
import com.m2micro.m2mfa.report.query.YieldQuery;
import com.m2micro.m2mfa.report.service.YieldService;
import com.m2micro.m2mfa.report.vo.Yield;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 生产产量报表
 */
@RestController
@RequestMapping("/report/Yield")
@Api(value="看板配置项")
public class YieldController {

  @Autowired
  private YieldService  yieldService ;

  /**
   * 生产产量报表
   */
  @PostMapping("/YieldShow")
  @ApiOperation(value="生产产量报表显示")
  @UserOperationLog("生产产量报表显示")
  public ResponseMessage<List<Yield>> YieldShow(YieldQuery yieldQuery){

    return  ResponseMessage.ok(yieldService.YieldShow(yieldQuery));
  }


  /**
   * exce导出生产产量报表
   */
  @PostMapping("/excelOutData")
  @ApiOperation(value="exce导出生产产量报表")
  @UserOperationLog("exce导出生产产量报表")
  public ResponseMessage excelOutData(YieldQuery yieldQuery,  HttpServletResponse response)throws Exception{
    yieldService.excelOutData(yieldQuery ,response);
    return  ResponseMessage.ok();
  }


 /* public static void main(String[] args) throws Exception {
    // 建立一个Excel
     Workbook book = new HSSFWorkbook();
    HSSFCellStyle style2 = getHssfCellStyle(book);

    // 在对应的Excel中建立一个分表
    Sheet sheet1 =(Sheet) book.createSheet("分表1");
    //合并单元格
    sheet1.addMergedRegion(new CellRangeAddress(0,3,1,1));
    sheet1.addMergedRegion(new CellRangeAddress(4,6,1,1));
    //设置缺省列高
    sheet1.setDefaultRowHeightInPoints(20);
    //设置缺省列宽
    sheet1.setDefaultColumnWidth(20);
    for(int i=0;i<10;i++){
      // 设置相应的行（初始从0开始）
      Row row =sheet1.createRow(i);
      // 在所在的行设置所在的单元格（相当于列，初始从0开始,对应的就是A列）
      Cell cell = row.createCell(0);
      cell.setCellStyle(style2);
      // 写入相关数据到设置的行列中去。
      cell.setCellValue("相关数据"+i);
      Cell cell1 = row.createCell(1);
      cell1.setCellStyle(style2);

      cell1.setCellValue("相关数据"+i);
    }

    // 保存到计算机相应路径
     book.write( new FileOutputStream("D://a.xls")); }

  private static HSSFCellStyle getHssfCellStyle(Workbook book) {
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
*/
 public static void main(String[] args) throws IOException
 {

     String html = PDFKit.readFileByUrl("http://127.0.0.1/export/1/1"); // 将html代码读取到html字符串中

   try {
     Document document = new Document();
     PdfWriter mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(new File("C:\\data\\3.pdf")));
     document.open();
     ByteArrayInputStream bin = new ByteArrayInputStream(html.getBytes());
     XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, null, new ChinaFontProvide());
     System.out.println("生成完毕");
     document.close();
   } catch (Exception e) {
     e.printStackTrace();
   }
 }

  public static final class ChinaFontProvide implements FontProvider
  {


    public boolean isRegistered(String s)
    {
      return false;
    }


    public Font getFont(String arg0, String arg1, boolean arg2, float arg3, int arg4, BaseColor arg5)
    {
      BaseFont bfChinese = null;
      try
      {
        bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
      return FontChinese;
    }


  }



}
