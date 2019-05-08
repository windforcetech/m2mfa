package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "不良查询条件")
@Data
public class BaseDefectQuery extends Query {
  @ApiModelProperty(value = "不良代码")
  private String ectCode;
  @ApiModelProperty(value = "不良名称")
  private String ectName;
  @ApiModelProperty(value = "状态")
  private Boolean enabled ;

  private String description;

  private String category;


}
