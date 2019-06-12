package com.m2micro.m2mfa.report.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributedQuery {
  @ApiModelProperty(value = "工单号码")
  private String moNumber;
  @ApiModelProperty(value = "料件编号")
  private String partNo;
}
