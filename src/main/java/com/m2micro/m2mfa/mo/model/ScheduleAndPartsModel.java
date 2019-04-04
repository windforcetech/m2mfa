package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ScheduleAndPartsModel", description = "排产单料件信息")
public class ScheduleAndPartsModel {
    @ApiModelProperty(value = "排产id")
    private String scheduleId;
    @ApiModelProperty(value = "排产单号")
    private String scheduleNo;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "规格")
    private String partSpec;
}
