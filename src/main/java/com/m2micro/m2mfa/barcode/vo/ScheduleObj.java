package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
@ApiModel("排产单")
public class ScheduleObj {
    @ApiModelProperty("排产单id")
    private String scheduleId;
    @ApiModelProperty("排产单号")
    private String scheduleNo;
    @ApiModelProperty("机台id")
    private String machineId;
    @ApiModelProperty("料件id")
    private String partId;
    @ApiModelProperty("料件编号")
    private String partNo;
    @ApiModelProperty("品名")
    private String partName;
    @ApiModelProperty("排产数量")
    private Integer scheduleQty;
    @ApiModelProperty("机台编号")
    private String machineName;

    @ApiModelProperty("规格")
    private String partSpec;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户编号")
    private String customerCode;

    @ApiModelProperty("料件关联模板信息")
    private List<TemplateObj> templateObjList;

    @ApiModelProperty("项次")
    private Integer orderSeq;


}
