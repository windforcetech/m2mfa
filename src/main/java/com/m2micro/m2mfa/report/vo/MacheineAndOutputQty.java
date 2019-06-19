package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MacheineAndOutputQty {
  @ApiModelProperty(value = "机台名称")
  String machineName;
  @ApiModelProperty(value = "机台产量")
  long outputQty;
}
