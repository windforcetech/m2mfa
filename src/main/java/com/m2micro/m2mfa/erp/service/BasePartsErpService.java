package com.m2micro.m2mfa.erp.service;

public interface BasePartsErpService {

  //料件erp导入
  boolean erpParts(String partNos,Long x,Long y);

  Long erpPartsCount(String partNos);
}
