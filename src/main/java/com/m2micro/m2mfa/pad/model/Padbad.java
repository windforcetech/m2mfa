package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.record.entity.MesRecordFail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Padbad", description = "Pad不良参数")
public class Padbad {

  @ApiModelProperty(value = "工位Id")
  private String stationId;
  @ApiModelProperty(value = "不良输入")
  private MesRecordFail mesRecordFail;
}
