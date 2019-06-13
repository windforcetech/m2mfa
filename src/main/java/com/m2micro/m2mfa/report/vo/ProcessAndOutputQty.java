package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessAndOutputQty {
  @ApiModelProperty(value = "工序")
  String processName;
  @ApiModelProperty(value = "工序产量")
  long outputQty;
}
