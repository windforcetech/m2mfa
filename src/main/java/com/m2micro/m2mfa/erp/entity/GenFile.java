package com.m2micro.m2mfa.erp.entity;

import lombok.Data;

@Data
public class GenFile {
  private String gen02;
  private String  gen01;
  private String  gen03;
  private String  gen04;
  private String  gen06;
  private String  gen05;

  public String getGen02() {
    return gen02;
  }

  public void setGen02(String gen02) {
    this.gen02 = gen02;
  }

  public String getGen01() {
    return gen01;
  }

  public void setGen01(String gen01) {
    this.gen01 = gen01;
  }

  public String getGen03() {
    return gen03;
  }

  public void setGen03(String gen03) {
    this.gen03 = gen03;
  }

  public String getGen04() {
    return gen04;
  }

  public void setGen04(String gen04) {
    this.gen04 = gen04;
  }

  public String getGen06() {
    return gen06;
  }

  public void setGen06(String gen06) {
    this.gen06 = gen06;
  }

  public String getGen05() {
    return gen05;
  }

  public void setGen05(String gen05) {
    this.gen05 = gen05;
  }
}
