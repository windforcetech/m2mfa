package com.m2micro.m2mfa.common.util;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
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
  public static void excelDownloadFlie(HttpServletResponse response, Workbook book,String fileName) {
    response.reset(); // 清除buffer缓存
    Date date =new Date();
    SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHHmmss");
    String time =sdf.format(date);
    response.setHeader("Content-Disposition", "attachment;filename="+ fileName+time +".xlsx");
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
  /**
   * 支持在线打开和下载
   *
   * @param filePath
   * @param response
   * @param isOnLine
   * @param fname
   * @throws IOException
   */
  public static void download(String filePath, HttpServletResponse response,
                       boolean isOnLine, String fname) throws IOException {
    System.out.println("filePath:" + filePath);
    File f = new File(filePath);
    if (!f.exists()) {
      response.sendError(404, "File not found!");
      return;
    }
    BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
    byte[] bs = new byte[1024];
    int len = 0;
    response.reset(); // 非常重要
    if (isOnLine) { // 在线打开方式
      URL u = new URL("file:///" + filePath);
      String contentType = u.openConnection().getContentType();
      response.setContentType(contentType);
      response.setHeader("Content-Disposition", "inline;filename="
          + fname);
      // 文件名应该编码成utf-8，注意：使用时，我们可忽略这句
    } else {
      // 纯下载方式
      response.setContentType("application/x-msdownload");
      response.setHeader("Content-Disposition", "attachment;filename="
          + fname);
    }
    OutputStream out = response.getOutputStream();
    while ((len = br.read(bs)) > 0) {
      out.write(bs, 0, len);
    }
    out.flush();
    out.close();
    br.close();
  }



}
