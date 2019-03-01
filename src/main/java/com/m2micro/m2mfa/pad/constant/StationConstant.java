package com.m2micro.m2mfa.pad.constant;

/**
 * 工位类型
 */
public enum StationConstant {
  TUNING("KZ029","调机"),
  FRAMEMOLD("KZ027","架模"),
  FEEDING("ZS_CX_01","加料"),
  BAKING("ZS_CX_02","烤料"),
  BOOT("KZ001","开机");

  private final String key;
  private final String value;
  StationConstant(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }
  public String getValue() {
    return value;
  }

}
