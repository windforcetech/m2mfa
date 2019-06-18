package com.m2micro.m2mfa.report.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BootAndData {

  @ApiModelProperty(value = "日期")
  private Date Time ;


  @ApiModelProperty(value = "汇总")
  private long summary ;


  @ApiModelProperty(value = "总达成率")
  private long achievingRate ;


  @ApiModelProperty(value = "总不良")
  private long bad ;


  @ApiModelProperty(value = "总工时")
  private long hours ;


  @ApiModelProperty(value = "人均值")
  private long mean ;

  @ApiModelProperty(value = "班别数据")
  private List<ShiftAndData> shiftAndDatas;

  @ApiModelProperty(value = "详情")
  private List<Boot> boots;

}
