package com.m2micro.m2mfa.common.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class ChinaFontProvide implements FontProvider
{


  public boolean isRegistered(String s)
  {
    return false;
  }


  public Font getFont(String arg0, String arg1, boolean arg2, float arg3, int arg4, BaseColor arg5)
  {
    BaseFont bfChinese = null;

    try {
      bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }


    Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
    return FontChinese;
  }
}
