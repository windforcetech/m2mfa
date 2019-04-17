package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "料号模板关联查询obj")
public class BasePartTemplateQuery extends Query {
  @ApiModelProperty(value = "料件编号")
  private String partNumber;
  @ApiModelProperty(value = "模板名称")
  private String templateName;

  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }
}
