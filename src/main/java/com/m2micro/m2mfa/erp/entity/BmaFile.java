package com.m2micro.m2mfa.erp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BmaFile {
  private String  bma01;
  private String  bma06;
  private Date bma05;

  public String getBma01() {
    return bma01;
  }

  public void setBma01(String bma01) {
    this.bma01 = bma01;
  }

  public String getBma06() {
    return bma06;
  }

  public void setBma06(String bma06) {
    this.bma06 = bma06;
  }

  public Date getBma05() {
    return bma05;
  }

  public void setBma05(Date bma05) {
    this.bma05 = bma05;
  }
}
