package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class DistributedDate {
  @ApiModelProperty(value = "详情数据")
  List<Distributed>  distributeds;
  @ApiModelProperty(value = "工序数据显示")
 List< ProcessAndOutputQty> processAndOutputQtys;
  @ApiModelProperty(value = "机台数据显示")
  List< MacheineAndOutputQty> macheineAndOutputQtys;
}
