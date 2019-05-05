package com.m2micro.m2mfa.barcode.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("分页查询打印申请")
public class PrintApplyQuery extends Query {

    @ApiModelProperty("开始日期")
    private String startDate;
    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("申请单号")
    private String applyId;
    @ApiModelProperty("申请类型")
    private String printCategory;
    @ApiModelProperty("来源类型Id")
    private String category;
    @ApiModelProperty("来源类型")
    private String sourceType;

    @ApiModelProperty("来源类型Id")
    private String sourceId;
    @ApiModelProperty("来源单号id")
    private String source;

    @ApiModelProperty("来源单号")
    private String scheduleNo;
    @ApiModelProperty("标签类型")
    private String flagType;
    @ApiModelProperty("标签模板")
    private String templateName;
    @ApiModelProperty("模板id")
    private String templateId;
    @ApiModelProperty("版本")
    private Integer templateVersion;
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户编号")
    private String customerCode;
    @ApiModelProperty("料件编号")
    private String partNo;

    @ApiModelProperty("状态")
    private String flag;


    @ApiModelProperty("审核")
    private String checkFlag;

    @ApiModelProperty("有效")
    private Boolean enabled;

    @ApiModelProperty("规格")
    private String spec;

    @ApiModelProperty("品名")
    private String partName;


}
