package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Padbad", description = "Pad不良参数")
public class Padbad {
  @ApiModelProperty(value = "上工记录Id")
  private String rwId;
  @ApiModelProperty(value = "不良编码")
  private String dctCode;
  @ApiModelProperty(value = "数量")
  private Integer qty;
  @ApiModelProperty(value = "工位Id")
  private String stationId;
}
