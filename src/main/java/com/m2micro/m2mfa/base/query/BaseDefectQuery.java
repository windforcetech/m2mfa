package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "不良查询条件")
public class BaseDefectQuery extends Query {
  @ApiModelProperty(value = "不良代码")
  private String ectCode;
  @ApiModelProperty(value = "不良名称")
  private String ectName;

  public String getEctCode() {
    return ectCode;
  }

  public void setEctCode(String ectCode) {
    this.ectCode = ectCode;
  }

  public String getEctName() {
    return ectName;
  }

  public void setEctName(String ectName) {
    this.ectName = ectName;
  }
}
