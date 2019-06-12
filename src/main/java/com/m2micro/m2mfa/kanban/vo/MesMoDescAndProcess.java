package com.m2micro.m2mfa.kanban.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MesMoDescAndProcess {
  @ApiModelProperty(value = "工单号码")
  private String moNumber ;
  @ApiModelProperty(value = "客户名称")
  private String customerName ;
  @ApiModelProperty(value = "料件名称")
  private String partName ;
  @ApiModelProperty(value = "料件编号")
  private String partNo ;
  @ApiModelProperty(value = "达交时间")
  private String reachDate ;
  @ApiModelProperty(value = "工单数量")
  private Long mesMoDescTargetQty ;
  @ApiModelProperty(value = "工单产量")
  private Long mesMoDescOutputQty ;
  @ApiModelProperty(value = "工序名称")
  private String processName ;
  @ApiModelProperty(value = "工序产量 ")
  private Long  processOutputQty ;
  @ApiModelProperty(value = "状态")
  private String closeFlag ;
}
