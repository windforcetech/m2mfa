package com.m2micro.m2mfa.kanban.constant;

/**
 * 机台状态
 */
public enum  MachineConstant {
  PRODUCE("MACHINE_STATE_0","生产"),
  TUNING("MACHINE_STATE_1","调机"),
  SERVICE("MACHINE_STATE_2","维修"),
  MAINTENANCE("MACHINE_STATE_3","保养"),
  DOWNTIME("MACHINE_STATE_4","停机");
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
