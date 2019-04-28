package com.m2micro.m2mfa.erp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BmbFile {
  private Integer  bmb02;
  private String  bmb03;
  private String  bmb29;
  private Date  bmb04;
  private Date  bmb05;
  private BigDecimal bmb06;
  private String  bmb10;
  private Integer  bmb07;
  private BigDecimal  bmb08;
  private Boolean  bmb16;
  private Boolean  bmb19;

  public Integer getBmb02() {
    return bmb02;
  }

  public void setBmb02(Integer bmb02) {
    this.bmb02 = bmb02;
  }

  public String getBmb03() {
    return bmb03;
  }

  public void setBmb03(String bmb03) {
    this.bmb03 = bmb03;
  }

  public String getBmb29() {
    return bmb29;
  }

  public void setBmb29(String bmb29) {
    this.bmb29 = bmb29;
  }

  public Date getBmb04() {
    return bmb04;
  }

  public void setBmb04(Date bmb04) {
    this.bmb04 = bmb04;
  }

  public Date getBmb05() {
    return bmb05;
  }

  public void setBmb05(Date bmb05) {
    this.bmb05 = bmb05;
  }

  public BigDecimal getBmb06() {
    return bmb06;
  }

  public void setBmb06(BigDecimal bmb06) {
    this.bmb06 = bmb06;
  }

  public String getBmb10() {
    return bmb10;
  }

  public void setBmb10(String bmb10) {
    this.bmb10 = bmb10;
  }

  public Integer getBmb07() {
    return bmb07;
  }

  public void setBmb07(Integer bmb07) {
    this.bmb07 = bmb07;
  }

  public BigDecimal getBmb08() {
    return bmb08;
  }

  public void setBmb08(BigDecimal bmb08) {
    this.bmb08 = bmb08;
  }

  public Boolean getBmb16() {
    return bmb16;
  }

  public void setBmb16(Boolean bmb16) {
    this.bmb16 = bmb16;
  }

  public Boolean getBmb19() {
    return bmb19;
  }

  public void setBmb19(Boolean bmb19) {
    this.bmb19 = bmb19;
  }
}
