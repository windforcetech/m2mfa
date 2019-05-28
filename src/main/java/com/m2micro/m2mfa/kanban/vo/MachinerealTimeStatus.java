package com.m2micro.m2mfa.kanban.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MachinerealTimeStatus {
  @ApiModelProperty(value = "机台名称")
  private String machineName ;
  @ApiModelProperty(value = "机台状态")
  private String machineStatus ;
  @ApiModelProperty(value = "工单号")
  private String moNumber ;
  @ApiModelProperty(value = "目标量")
  private String targetQty ;
  @ApiModelProperty(value = "完成量")
  private String schedulQty ;
  @ApiModelProperty(value = "完成率")
  private String rate ;
}
