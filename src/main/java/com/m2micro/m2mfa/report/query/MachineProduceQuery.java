package com.m2micro.m2mfa.report.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class MachineProduceQuery {

  @ApiModelProperty(value = "机台号")
  private String []  machineids;

  @ApiModelProperty(
      value = "日期",
      example = "2018-11-11 12:00:00"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  private Date  startTime;


  @ApiModelProperty(value = "班别")
  private String  shiftId;

}
