package com.m2micro.m2mfa.barcode.constant;

/**
 * 编码生成规则定义
 */
public enum  BarcodeConstant
{
  FIXED_CODE("10000310","固定码"),
  FLOWING_CODE("10000311","流水码"),
  DATE_FUNCTION("10000312","日期函数"),
  WORK_ORDER_NUMBER("10000313","工单号码"),
  ITEM_NUMBER("10000314","料件编号"),
  NET_WEIGHT("10000315","净重"),
  BOXING_QUANTITY("10000316","装箱数量"),
  GROSS_WEIGHT("10000336","毛重"),
  VOLUME("10000337","材积"),
  PART_NAME("10000338","料件品名"),
  MATERIAL_SPECIFICATIONS("10000339","料件规格");

  private final String key;
  private final String value;


  BarcodeConstant(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String  getKey() {
    return key;
  }
  public String getValue() {
    return value;
  }
  public static BarcodeConstant barcodevalueOf(String  key) {
    BarcodeConstant ms = null;
    switch (key){
      case "10000313":
        ms = BarcodeConstant.WORK_ORDER_NUMBER;
        break;
      case "10000314":
        ms = BarcodeConstant.ITEM_NUMBER;
        break;
      case "10000315":
        ms = BarcodeConstant.NET_WEIGHT;
        break;
      case "10000316":
        ms = BarcodeConstant.BOXING_QUANTITY;
        break;
      case "10000336":
        ms = BarcodeConstant.GROSS_WEIGHT;
        break;
      case "10000337":
        ms = BarcodeConstant.VOLUME;
        break;
      case "10000338":
        ms = BarcodeConstant.PART_NAME;
        break;
      case "10000339":
        ms = BarcodeConstant.MATERIAL_SPECIFICATIONS;
        break;
      case "10000310":
        ms = BarcodeConstant.FIXED_CODE;
        break;
      case "10000311":
        ms = BarcodeConstant.FLOWING_CODE;
        break;
      case "10000312":
        ms = BarcodeConstant.DATE_FUNCTION;
        break;
      default:
        ms = BarcodeConstant.DATE_FUNCTION;
    }
    return  ms;
  }
}
