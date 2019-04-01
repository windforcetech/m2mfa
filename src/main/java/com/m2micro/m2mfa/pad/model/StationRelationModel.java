package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "StationRelationModel", description = "过站关联参数")
public class StationRelationModel {
    @ApiModelProperty(value = "排产单号")
    private String scheduleNo;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "规格")
    private String partSpec;
    @ApiModelProperty(value = "上一工序")
    private String wipNowProcessName;
    @ApiModelProperty(value = "作业职员")
    private String staffName;
    @ApiModelProperty(value = "产出时间")
    private Date outTime;
    @ApiModelProperty(value = "产出数")
    private Integer outputQty;
    @ApiModelProperty(value = "采集方式")
    private String collectionName;
    @ApiModelProperty(value = "下一工序")
    private String wipNextProcessName;
}
