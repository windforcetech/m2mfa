package com.m2micro.m2mfa.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
}
