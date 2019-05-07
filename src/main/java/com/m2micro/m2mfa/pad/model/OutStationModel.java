package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.common.validator.QueryGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "OutStationModel", description = "出站参数")
public class OutStationModel {
    @ApiModelProperty(value = "条码标签")
    @NotEmpty(message="条码标签不能为空",groups = {QueryGroup.class})
    private String barcode;
    @ApiModelProperty(value = "工序主键")
    @NotEmpty(message="工序主键不能为空",groups = {QueryGroup.class})
    private String processId;
    @ApiModelProperty(value = "工位Id")
    @NotEmpty(message="工位Id不能为空",groups = {QueryGroup.class})
    private String stationId;
    @ApiModelProperty(value = "产出数")
    @NotNull(message="产出数不能为空",groups = {QueryGroup.class})
    private Integer outputQty;
    @ApiModelProperty(value = "不良数量")
    @NotNull(message="不良数量不能为空",groups = {QueryGroup.class})
    private Long qty;
    @ApiModelProperty(value = "不良数ids")
    private ArrayList<String> ids;
    @ApiModelProperty(value = "不良输入")
    @Valid
    private ArrayList<CrossStationFail> crossStationFails;
}
