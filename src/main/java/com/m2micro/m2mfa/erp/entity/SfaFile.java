package com.m2micro.m2mfa.erp.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SfaFile {
  private String  sfa03;
  private String  sfa27;
  private String sfa12;
  private BigDecimal sfa16;
  private BigDecimal  sfa05;

  public String getSfa03() {
    return sfa03;
  }

  public void setSfa03(String sfa03) {
    this.sfa03 = sfa03;
  }

  public String getSfa27() {
    return sfa27;
  }

  public void setSfa27(String sfa27) {
    this.sfa27 = sfa27;
  }

  public String getSfa12() {
    return sfa12;
  }

  public void setSfa12(String sfa12) {
    this.sfa12 = sfa12;
  }

  public BigDecimal getSfa16() {
    return sfa16;
  }

  public void setSfa16(BigDecimal sfa16) {
    this.sfa16 = sfa16;
  }

  public BigDecimal getSfa05() {
    return sfa05;
  }

  public void setSfa05(BigDecimal sfa05) {
    this.sfa05 = sfa05;
  }
}
