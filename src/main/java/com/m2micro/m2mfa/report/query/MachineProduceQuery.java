package com.m2micro.m2mfa.report.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MachineProduceQuery {
  @ApiModelProperty(value = "机台ids")
  private String []  machineids;



}
