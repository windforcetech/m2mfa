package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MachineProduce {

  @ApiModelProperty(value = "机台号")
  private String   machineName;

  @ApiModelProperty(value = "班别")
  private String   shiftName;

  @ApiModelProperty(value = "料件")
  private String   partNo;

  @ApiModelProperty(value = "品名")
  private String   partName;

  @ApiModelProperty(value = "模号")
  private String   moldName;

  @ApiModelProperty(value = "客户料件号")
  private String   customerPartNo;

  @ApiModelProperty(value = "材质")
  private String   material;

  @ApiModelProperty(value = "作业人数")
  private long   staffNumber;

  @ApiModelProperty(value = "成型周期")
  private String   standardHours;

  @ApiModelProperty(value = "机台工时")
  private String   machineTimeCost;

  @ApiModelProperty(value = "模具穴数")
  private String   cavityQty;

  @ApiModelProperty(value = "生产穴数")
  private String   cavityAvailable;


  @ApiModelProperty(value = "开模次数")
  private long   cavityNumber;

  @ApiModelProperty(value = "目标产能")
  private long   scheduleQty;


  @ApiModelProperty(value = "实际产能")
  private String   machineMolds;

  @ApiModelProperty(value = "良品数量")
  private String   goodProductNumber;

  @ApiModelProperty(value = "不良数量")
  private String   failQty;

  @ApiModelProperty(value = "不良率")
  private String   failQtyRate;
}
