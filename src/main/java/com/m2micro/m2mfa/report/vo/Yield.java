package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Yield {
  @ApiModelProperty(value = "工单号码")
  private String moNumber;
  @ApiModelProperty(value = "工单状态")
  private String closeFlag;
  @ApiModelProperty(value = "物料编号")
  private String partNo;
  @ApiModelProperty(value = "物料名称")
  private String partName;
  @ApiModelProperty(value = "规格")
  private String spec;
  @ApiModelProperty(value = "计划数")
  private long targetQty;
  @ApiModelProperty(value = "单位")
  private String productionUnit;
  @ApiModelProperty(value = "工序名称")
  private String processName;
  @ApiModelProperty(value = "完成量")
  private long outputQty;
  @ApiModelProperty(value = "不良数量")
  private long failQty;
}
