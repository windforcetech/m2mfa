package com.m2micro.m2mfa.kanban.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MesMoDescTime {
  @ApiModelProperty(value = "工序集合")
  private Set<String> processnames;
  @ApiModelProperty(value = "工单数据")
  private Set<MesMoDescTimeData> mesMoDescTimeDatas ;
}
