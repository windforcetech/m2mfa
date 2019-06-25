package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PostProcess {

  @ApiModelProperty(value = "归属日期")
  private Date outTime ;


  @ApiModelProperty(value = "工序名称")
  private String  processName ;

  @ApiModelProperty(value = "料件编号")
  private String  partNo ;

  @ApiModelProperty(value = "料件名称")
  private String  partName ;


  @ApiModelProperty(value = "班别名称")
  private String  shiftName;


  @ApiModelProperty(value = "op代号")
  private String  staffCode;

  @ApiModelProperty(value = "op姓名")
  private String  staffName;


  @ApiModelProperty(value = "op工时")
  private String   workinghours;


  @ApiModelProperty(value = "生产数")
  private long   outputQty;

  @ApiModelProperty(value = "达成数")
  private String   reachNumber;

  @ApiModelProperty(value = "达成率")
  private String   achievingRate;

  @ApiModelProperty(value = "报废数")
  private long   scrapQty;

  @ApiModelProperty(value = "不良率")
  private String   failQtyRate;

  @ApiModelProperty(value = "模具名称 ")
  private String   moldName;
}
