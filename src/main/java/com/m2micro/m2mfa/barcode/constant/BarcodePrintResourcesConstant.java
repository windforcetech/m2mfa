package com.m2micro.m2mfa.barcode.constant;

public enum BarcodePrintResourcesConstant {
  //审核打印 :1, 已打印：2， 打印作废:-1,未审核：0
  AUDIT_PRINT(1,"审核打印"),
  PRINTED(2,"已打印"),
  PRINT_INVALID(-1,"打印作废"),
  UNREVIEWED(0,"未审核");


  private final Integer key;
  private final String value;
  BarcodePrintResourcesConstant(Integer key, String value) {
    this.key = key;
    this.value = value;
  }
  public Integer getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
