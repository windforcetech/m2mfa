package com.m2micro.m2mfa.kanban.constant;

/**
 * 机台状态
 */
public enum  MachineConstant {
  PRODUCE("10000950","生产"),
  TUNING("10000951","调机"),
  SERVICE("10000952","维修"),
  MAINTENANCE("10000953","保养"),
  DOWNTIME("10000954","停机");
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
