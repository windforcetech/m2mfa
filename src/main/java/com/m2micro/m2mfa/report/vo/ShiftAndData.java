package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ShiftAndData {

  @ApiModelProperty(value = "日期")
  private Date startTime ;

  @ApiModelProperty(value = "班别名称")
  private String   shiftName ;


  @ApiModelProperty(value = "班别汇总")
  private long shiftSummary ;


  @ApiModelProperty(value = "班别总达成率")
  private long shiftAchievingRate ;


  @ApiModelProperty(value = "班别总不良")
  private long shiftBad ;


  @ApiModelProperty(value = "班别总工时")
  private String shiftHours ;


  @ApiModelProperty(value = "班别人均值")
  private long shiftMean ;



}
