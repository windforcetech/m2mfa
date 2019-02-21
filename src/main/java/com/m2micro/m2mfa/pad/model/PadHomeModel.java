package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description="pad主页显示信息")
@Data
@Builder
public class PadHomeModel {


  @ApiModelProperty(value = "机台名称")
  private String machineName;

  @ApiModelProperty(value = "机台采集方式")
  private String collection;

  @ApiModelProperty(value = "已加料量")
  private Integer partInput;

  @ApiModelProperty(value = "已使用量")
  private Integer partOutput;

  @ApiModelProperty(value = "料桶余量")
  private Integer partRemaining;

  @ApiModelProperty(value = "职业编号")
  private String staffCode;

  @ApiModelProperty(value = "职业名称")
  private String staffName;

  @ApiModelProperty(value = "职业所属部门")
  private String staffDepartmentName;

  @ApiModelProperty(value = "班别")
  private String staffShiftName;

  @ApiModelProperty(value = "上岗时间")
  private Date staffOnTime;

  @ApiModelProperty(value = "执行标准")
  private long standardOutput;

  @ApiModelProperty(value = "实际产出")
  private long actualOutput;

  @ApiModelProperty(value = "达成率")
  private long  rate;

}
