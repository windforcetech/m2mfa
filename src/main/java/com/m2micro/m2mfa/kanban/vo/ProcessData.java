package com.m2micro.m2mfa.kanban.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProcessData {
  @ApiModelProperty(value = "工序名称")
  private String processName ;
  @ApiModelProperty(value = "工序产量 ")
  private Long  processOutputQty ;
}
