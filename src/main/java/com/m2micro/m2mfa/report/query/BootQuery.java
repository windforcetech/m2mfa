package com.m2micro.m2mfa.report.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BootQuery {
  @ApiModelProperty(
      value = "开机时间",
      example = "2018-11-11 12:00:00"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  private Date bootTime;
}
