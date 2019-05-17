package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "班别基本资料")
@Data
public class BaseShiftQuery  extends Query {

  @ApiModelProperty(value = "是否有效 ")
  private Boolean enabled;
  @ApiModelProperty(value = "编号")
  private String code;
  @ApiModelProperty(value = "名称")
  private String name;
  @ApiModelProperty(value = "类型")
  private String category;
  @ApiModelProperty(value = "描述")
  private String description;



}
