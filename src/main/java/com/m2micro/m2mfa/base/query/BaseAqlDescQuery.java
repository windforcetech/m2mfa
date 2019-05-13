package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="抽样参数")
@Data
public class BaseAqlDescQuery extends Query {
  @ApiModelProperty(value = "抽样编号")
  private String aqlCode;
  @ApiModelProperty(value = "抽样名称")
  private String aqlName;
  @ApiModelProperty(value = "抽样方式")
  private String category;
  @ApiModelProperty(value = "有效否")
  private Boolean  enabled;
  @ApiModelProperty(value = "描述")
  private String  description;

}
