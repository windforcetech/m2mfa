package com.m2micro.m2mfa.report.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class YieldQuery {
  @ApiModelProperty(value = "工单号码")
  private String moNumber;
  @ApiModelProperty(value = "工序名称")
  private String processName;
  @ApiModelProperty(value = "物料编号")
  private String partNo;
  @ApiModelProperty(value = "物料类型")
  private String category;
  @ApiModelProperty(value = "工单完成")
  private boolean moflag;
  @ApiModelProperty(value = "工序完成")
  private boolean processflag;
  @ApiModelProperty(value = "排单完成")
  private boolean cheduleflag;
  @ApiModelProperty(
      value = "生产时间",
      example = "2018-11-11 12:00:00"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  private Date produceTime;
  @ApiModelProperty(value = " 本月 (month),上个月(onmonth),今天(day),昨天(yesterday),本周(week),上周(onweek),本年(year),上年(onyear),本季(season),上季(onseason)")
  private String timecondition;
}