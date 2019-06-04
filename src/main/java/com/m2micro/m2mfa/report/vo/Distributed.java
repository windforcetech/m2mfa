package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Distributed {
   @ApiModelProperty(value = "料件编号")
  private String partNo;

  @ApiModelProperty(value = "料件名称 ")
  private String partName;

  @ApiModelProperty(value = "规格")
  private String spec;

  @ApiModelProperty(value = "工单号码")
  private String  moNumber;

  @ApiModelProperty(value = "排查单号码")
  private  String  scheduleNo;

  @ApiModelProperty(value = "工序名称")
  private String  processName;

  @ApiModelProperty(value = "产出数量")
  private long outputQty;

  @ApiModelProperty(value = "机台名称")
  private String machineName;

}
