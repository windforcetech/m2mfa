package com.m2micro.m2mfa.pad.model;


import com.m2micro.m2mfa.common.validator.QueryGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(value = "CrossingStationModel", description = "过站返回参数")
public class CrossingStationModel {
    @ApiModelProperty(value = "条码标签")
    private String barcode;
    @ApiModelProperty(value = "产出数")
    private Integer outputQty;
    @ApiModelProperty(value = "不良数量")
    private Long qty;
    @ApiModelProperty(value = "不良数ids")
    private List ids;
    @ApiModelProperty(value = "结余量")
    private Integer surplusQty;
    @ApiModelProperty(value = "过站关联参数")
    private StationRelationModel stationRelationModel;


}
