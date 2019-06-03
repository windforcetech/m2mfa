package com.m2micro.m2mfa.common.util;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
  /**
   * 文件下载
   * @param fileName
   * @param filepath
   * @param response
   * @throws IOException
   */
  public static  void downloadFile(String fileName,String filepath, HttpServletResponse response) throws IOException {
    response.setHeader("content-type", "application/octet-stream");
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    byte[] buffer = new byte[1024];
    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(filepath)));
    int read = bufferedInputStream.read(buffer);
    while (read > 0) {
      response.getOutputStream().write(buffer, 0, read);
      read = bufferedInputStream.read(buffer);
    }

  }

  public static void WriteStringToFile(String content) {
    FileOutputStream fos = null;
    try {
      //true不覆盖已有内容
      fos = new FileOutputStream("D:\\sql.txt", true);
      //写入
      fos.write(content.getBytes());
      // 写入一个换行
      fos.write("\r\n".getBytes());

    } catch (IOException e) {
      e.printStackTrace();
    }finally{
      if(fos != null){
        try {
          fos.flush();
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  /**
   * excel下载文件
   * @param response
   * @param book
   */
  public static void excelDownloadFlie(HttpServletResponse response, Workbook book) {
    response.reset(); // 清除buffer缓存
    Date date =new Date();
    SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
    String time =sdf.format(date);
    response.setHeader("Content-Disposition", "attachment;filename=yield" + time +".xlsx");
    response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    OutputStream output;
    try {
      output = response.getOutputStream();
      BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
      bufferedOutPut.flush();
      book.write(bufferedOutPut);
      bufferedOutPut.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
