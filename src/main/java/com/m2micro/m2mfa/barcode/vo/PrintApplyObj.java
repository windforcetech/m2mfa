package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("打印申请")
public class PrintApplyObj {
    @ApiModelProperty("申请单号")
    private String applyId;
    @ApiModelProperty("申请类型")
    private String printCategory;
    @ApiModelProperty("来源类型")
    private String category;
    @ApiModelProperty("来源单号")
    private String source;
    @ApiModelProperty("标签类型")
    private String flagType;
    @ApiModelProperty("标签模板")
    private String templateName;
    @ApiModelProperty("版本")
    private String templateVersion;
    @ApiModelProperty("客户")
    private String customerName;
    @ApiModelProperty("料件编号")
    private String partNo;
    @ApiModelProperty("数量")
    private String qty;
    @ApiModelProperty("申请时间")
    private Date checkOn;
    @ApiModelProperty("状态")
    private String flg;
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("审核")
    private Integer checkFlag;

    @ApiModelProperty("有效")
    private Boolean enabled;
}
