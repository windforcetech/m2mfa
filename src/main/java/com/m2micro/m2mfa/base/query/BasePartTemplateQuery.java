package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "料号模板关联查询obj")
public class BasePartTemplateQuery extends Query {
  @ApiModelProperty(value = "料件编号")
  private String partNo;
  @ApiModelProperty(value = "模板名称")
  private String templateName;
  @ApiModelProperty(value = "品名")
  private String partName;


}
