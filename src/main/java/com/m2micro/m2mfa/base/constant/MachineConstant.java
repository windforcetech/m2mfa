package com.m2micro.m2mfa.kanban.constant;

/**
 * 机台状态
 */
public enum  MachineConstant {
  PRODUCE("0","生产"),
  TUNING("1","调机"),
  SERVICE("2","维修"),
  MAINTENANCE("3","保养"),
  DOWNTIME("4","停机");
  private final String key;
  private final String value;


  MachineConstant(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String  getKey() {
    return key;
  }
  public String getValue() {
    return value;
  }
}
