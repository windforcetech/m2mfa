package com.m2micro.m2mfa.erp.service;

public interface BaseBomDescErpService {

  boolean erpBasebomdesc(String partNo, String distinguish,Long x,Long y) ;
  long erpBasebomdescCount(String partNo, String distinguish);
}
