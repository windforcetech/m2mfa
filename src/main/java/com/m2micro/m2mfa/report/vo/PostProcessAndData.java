package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostProcessAndData {

  @ApiModelProperty(value = "归属日期")
  private Date outTime ;


  @ApiModelProperty(value = "生产数")
  private Long   outputQty;

  @ApiModelProperty(value = "达成率")
  private String   achievingRate;

  @ApiModelProperty(value = "不良率")
  private String   failQtyRate;

  @ApiModelProperty(value = "op工时")
  private String   workinghours;

  @ApiModelProperty(value = "人均产量 ")
  private String   staffAverage;

  @ApiModelProperty(value = "后工序详情 ")
  private List<PostProcess> postProcesses;
}
