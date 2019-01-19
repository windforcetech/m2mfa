package com.m2micro.m2mfa.pad.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
  /**
   * 时间格式化成字符串
   * @param date Date
   * @return
   * @throws ParseException
   */
  public static String dateFormat(Date date)  {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }
}
