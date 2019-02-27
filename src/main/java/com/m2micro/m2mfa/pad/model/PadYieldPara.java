package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
@ApiModel(description="pad 主页参数")
@Data
@Builder
public class PadYieldPara {
  @ApiModelProperty(value = "排产单Id")
  private String scheduleId;


  @ApiModelProperty(value = "工位Id")
  private String stationId;

  @ApiModelProperty(value = "总量")
  private long totalamount;
}

