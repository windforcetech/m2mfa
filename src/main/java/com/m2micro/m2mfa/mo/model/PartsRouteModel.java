package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="料件综合信息")
public class PartsRouteModel {
    @ApiModelProperty(value = "料件ID")
    private String partId;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String name;
    @ApiModelProperty(value = "规格")
    private String spec;
    @ApiModelProperty(value = "工艺主表ID")
    private String  routeId;
    @ApiModelProperty(value = "工艺名称")
    private String routeName;
    @ApiModelProperty(value = "投入工序ID")
    private String inputProcessId;
    @ApiModelProperty(value = "投入工序名称")
    private String inputProcessName;
    @ApiModelProperty(value = "产出工序ID")
    private String outputProcessId;
    @ApiModelProperty(value = "产出工序名称")
    private String outputProcessName;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "bom版")
    private Integer bomRevsion;

}
