package com.m2micro.m2mfa.base.vo;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseInstructionQueryObj extends Query {
  @ApiModelProperty(value = "编号")
  private String instructionCode;
  @ApiModelProperty(value = "名称")
  private String instructionName;
  @ApiModelProperty(value = "有效否")
  private Boolean enabled;
  @ApiModelProperty(value = "类型")
  private String category;
  @ApiModelProperty(value = "发行否")
  private Boolean checkFlag;
  @ApiModelProperty(value = "描述")
  private String description;
  @ApiModelProperty(value = "版本")
  private Integer revsion;



}
