package com.m2micro.m2mfa.kanban.vo;

import com.m2micro.m2mfa.kanban.entity.BaseLedConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class MachinerealTimeData {
  @ApiModelProperty(value = "注塑设备")
  Integer machinerealTotal;
  @ApiModelProperty(value = "运行设备")
  Integer machinerealRun;
  @ApiModelProperty(value = "保养设备")
  Integer machinerealMaintenance;
  @ApiModelProperty(value = "故障设备")
  Integer machinereaMalfunction;
  @ApiModelProperty(value = "停机设备")
  Integer machinereaDowntime;
  @ApiModelProperty(value = "配置信息")
  BaseLedConfig baseLedConfig;
  @ApiModelProperty(value = "机台详情")
  List<MachinerealTimeStatus> machinerealTimeStatuses;
}
