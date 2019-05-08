package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.base.entity.BaseDefect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 过站不良信息
 */
@Data
@ApiModel(value = "CrossStationDefectModel", description = "过站不良信息")
public class CrossStationDefectModel {
    @ApiModelProperty(value = "过站不良工序")
    List<CrossStationProcess> crossStationProcesss;
    @ApiModelProperty(value = "过站不良现象")
    List<BaseDefect> baseDefects;
}
