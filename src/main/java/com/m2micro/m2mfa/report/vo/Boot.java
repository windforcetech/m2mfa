package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Boot {

  @ApiModelProperty(value = "归属日期")
  private Date startTime ;

  @ApiModelProperty(value = "班别")
  private String shiftName ;

  @ApiModelProperty(value = "机台")
  private String machineName ;

  @ApiModelProperty(value = "工号")
  private String staffCode ;

  @ApiModelProperty(value = "姓名")
  private String staffName ;

  @ApiModelProperty(value = "人力时间")
  private long useTime ;

  @ApiModelProperty(value = "机台工时")
  private long machineTime ;

  @ApiModelProperty(value = "品名")
  private String partName ;

  @ApiModelProperty(value = "模号")
  private String moldCode ;

  @ApiModelProperty(value = "模数")
  private Integer molds ;

  @ApiModelProperty(value = "穴数")
  private Integer cavityQty ;

  @ApiModelProperty(value = "目标")
  private Integer scheduleQty ;

  @ApiModelProperty(value = "达成率")
  private BigDecimal reach ;

  @ApiModelProperty(value = "生产数")
  private BigDecimal outputQty;

  @ApiModelProperty(value = "达成率")
  private BigDecimal achievingRate;

  @ApiModelProperty(value = "报废率")
  private BigDecimal scrapQty;

  @ApiModelProperty(value = "不良率")
  private BigDecimal failQty;
}
