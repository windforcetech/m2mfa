package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePartInstructionQuery extends Query {

  @ApiModelProperty(value = "料件编号")
  private String partNo;
  @ApiModelProperty(value = "编号")
  private String instructionCode;
  @ApiModelProperty(value = "有效否")
  private Boolean enabled;
  @ApiModelProperty(value = "描述")
  private String description;
}
