package com.m2micro.m2mfa.barcode.constant;

import com.m2micro.m2mfa.barcode.vo.PrintApplyObj;
import com.m2micro.m2mfa.barcode.vo.RuleObj;

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

  /**
   * 条码生成
   * @param key
   * @param dateNow
   * @param printApplyObj
   * @param allQty
   * @param n
   * @param i
   * @param rule
   * @return
   */
  public static String  barCodeGeneration(String  key, String dateNow, PrintApplyObj printApplyObj, Integer allQty, int n, Integer i, RuleObj rule) {
    String str = "";
    switch (key){
      case "10000313":
        str = printApplyObj.getMoNumber();
        break;
      case "10000314":
        str = printApplyObj.getPartNo();
        break;
      case "10000315":
        str = "" + printApplyObj.getPackObj().getNw().intValue();
        break;
      case "10000316":
        if (i == n) {
          str = "" + (allQty - (i - 1) * printApplyObj.getPackObj().getQty().intValue());
          System.out.println("ooo"+(allQty - (i - 1) * printApplyObj.getPackObj().getQty().intValue()));
        } else {
          str = "" + printApplyObj.getPackObj().getQty().intValue();
        }
        break;
      case "10000336":
        str = "" + printApplyObj.getPackObj().getGw().intValue();
        break;
      case "10000337":
        str = "" + printApplyObj.getPackObj().getCuft().intValue();
        break;
      case "10000338":
        str = printApplyObj.getPartName();
        break;
      case "10000339":
        str = printApplyObj.getSpec();
        break;
      case "10000310":
        str = rule.getDefaults();
        break;
      case "10000311":
        str = getSerialCode(rule,i);
        break;
      case "10000312":
        str = dateNow;
        break;
      default:
        break;
    }
    return  str;
  }

  /**
   * 生成流水号
   * @param rule
   * @param i
   * @return
   */
  public static String getSerialCode(RuleObj  rule,int i){
    //生成条码长度
    Integer length = rule.getLength()==null? 0: rule.getLength();
    //默认值
    Integer defaults = rule.getDefaults()==null || rule.getDefaults().trim().equals("") ? 0: Integer.parseInt(rule.getDefaults());
    if(defaults!=0){
      defaults=defaults-1;
    }
    //进制
    Integer ary = rule.getAry();
    String serialCode ="";
    String s = String.valueOf(i+defaults);
    int length1 = s.length();
    if(length1>=length){
      serialCode=s;
    }else {
      //s 补0
      int numlength =  length-length1;
      String seria="";
      for(int l = 0; l<numlength; l++){
        seria+="0";
      }
      serialCode=seria+s;
    }
    return  serialCode;
  }

}
