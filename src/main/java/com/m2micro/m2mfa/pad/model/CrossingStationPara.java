package com.m2micro.m2mfa.pad.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CrossingStationPara", description = "过站输入参数")
public class CrossingStationPara {

    @ApiModelProperty(value = "工位Id")
    private String stationId;
    @ApiModelProperty(value = "工序主键")
    private String processId;
}
