package com.m2micro.m2mfa.erp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SfbFile {
  private String  sfb01;
  private String  sfb02;
  private String  sfb05;
  private Integer  sfb08;
  private Integer  sfb07;
  private String sfb95;
  private String  sfb86;
  private Date sfb13;
  private Date  sfb15;
  private Date  sfb20;
  private String  sfb22;
  private Integer sfb221;

  public String getSfb01() {
    return sfb01;
  }

  public void setSfb01(String sfb01) {
    this.sfb01 = sfb01;
  }

  public String getSfb02() {
    return sfb02;
  }

  public void setSfb02(String sfb02) {
    this.sfb02 = sfb02;
  }

  public String getSfb05() {
    return sfb05;
  }

  public void setSfb05(String sfb05) {
    this.sfb05 = sfb05;
  }

  public Integer getSfb08() {
    return sfb08;
  }

  public void setSfb08(Integer sfb08) {
    this.sfb08 = sfb08;
  }

  public Integer getSfb07() {
    return sfb07;
  }

  public void setSfb07(Integer sfb07) {
    this.sfb07 = sfb07;
  }

  public String getSfb95() {
    return sfb95;
  }

  public void setSfb95(String sfb95) {
    this.sfb95 = sfb95;
  }

  public String getSfb86() {
    return sfb86;
  }

  public void setSfb86(String sfb86) {
    this.sfb86 = sfb86;
  }

  public Date getSfb13() {
    return sfb13;
  }

  public void setSfb13(Date sfb13) {
    this.sfb13 = sfb13;
  }

  public Date getSfb15() {
    return sfb15;
  }

  public void setSfb15(Date sfb15) {
    this.sfb15 = sfb15;
  }

  public Date getSfb20() {
    return sfb20;
  }

  public void setSfb20(Date sfb20) {
    this.sfb20 = sfb20;
  }

  public String getSfb22() {
    return sfb22;
  }

  public void setSfb22(String sfb22) {
    this.sfb22 = sfb22;
  }

  public Integer getSfb221() {
    return sfb221;
  }

  public void setSfb221(Integer sfb221) {
    this.sfb221 = sfb221;
  }
}
