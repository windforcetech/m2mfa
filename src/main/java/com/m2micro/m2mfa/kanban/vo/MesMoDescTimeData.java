package com.m2micro.m2mfa.kanban.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class MesMoDescTimeData {
  @ApiModelProperty(value = "工单号码")
  private String moNumber ;
  @ApiModelProperty(value = "客户名称")
  private String customerName ;
  @ApiModelProperty(value = "料件名称")
  private String partName ;
  @ApiModelProperty(value = "料件名称")
  private String partNo ;
  @ApiModelProperty(value = "达交时间")
  private String reachDate ;
  @ApiModelProperty(value = "工单数量")
  private Long mesMoDescTargetQty ;
  @ApiModelProperty(value = "工单产量")
  private Long mesMoDescOutputQty ;
  @ApiModelProperty(value = "工序数据")
  private String  closeFlag ;
  @ApiModelProperty(value = "工序数据")
  private Set<ProcessData >processDatas ;
}
