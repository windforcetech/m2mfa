package com.m2micro.m2mfa.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@Builder
@ApiModel(value="MesMoscheduleQueryObj", description="排产单条件")
public class MesMoscheduleQueryObj {

  @ApiModelProperty(value = "班别ID")
  private String shiftId;


  @ApiModelProperty(
      value = "预计开始时",
      example = "2018-11-11"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd"
  )
  private String shiftDate;
}
