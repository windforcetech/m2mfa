package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="抽样参数")
public class BaseAqlDescQuery extends Query {
  @ApiModelProperty(value = "抽样编号")
  private String aqlCode;
  @ApiModelProperty(value = "抽样名称")
  private String aqlName;

  public String getAqlCode() {
    return aqlCode;
  }

  public void setAqlCode(String aqlCode) {
    this.aqlCode = aqlCode;
  }

  public String getAqlName() {
    return aqlName;
  }

  public void setAqlName(String aqlName) {
    this.aqlName = aqlName;
  }
}
