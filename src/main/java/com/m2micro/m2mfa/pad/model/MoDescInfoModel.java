package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Auther: liaotao
 * @Date: 2019/2/19 14:03
 * @Description:
 */
@Data
@ApiModel(description="pad工单信息")
@EqualsAndHashCode
public class MoDescInfoModel {
    @ApiModelProperty(value = "工单号码")
    private String moNumber;
    @ApiModelProperty(value = "模具名称")
    private String moldName;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "规格")
    private String spec;
}
