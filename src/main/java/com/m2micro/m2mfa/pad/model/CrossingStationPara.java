package com.m2micro.m2mfa.pad.model;


import com.m2micro.m2mfa.common.validator.QueryGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "CrossingStationPara", description = "过站输入参数")
public class CrossingStationPara {
    @ApiModelProperty(value = "条码标签")
    @NotEmpty(message="条码标签不能为空",groups = {QueryGroup.class})
    private String barcode;
    //@ApiModelProperty(value = "工位Id")
    //@NotEmpty(message="工位Id不能为空",groups = {QueryGroup.class})
    //private String stationId;
    @ApiModelProperty(value = "工序主键")
    @NotEmpty(message="工序主键不能为空",groups = {QueryGroup.class})
    private String processId;
    @ApiModelProperty(value = "记录主键")
    @NotEmpty(message="记录主键不能为空",groups = {QueryGroup.class})
    private String rwId;
}
