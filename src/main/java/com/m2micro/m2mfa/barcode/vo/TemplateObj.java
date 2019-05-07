package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("模板信息")
public class TemplateObj {
    @ApiModelProperty("模板id")
    private String id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("模板描述")
    private String description;
    @ApiModelProperty("模板版本")
    private String version;
    @ApiModelProperty("标签类型id")
    private String tagId;
    @ApiModelProperty("标签类型名称")
    private String tagName;
}
