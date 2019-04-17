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
}
