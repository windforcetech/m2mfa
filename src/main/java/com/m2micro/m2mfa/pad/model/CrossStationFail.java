package com.m2micro.m2mfa.pad.model;


import com.m2micro.m2mfa.common.validator.QueryGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "CrossStationFail", description = "过站不良信息")
public class CrossStationFail {
    @ApiModelProperty(value = "工序主键")
    @NotEmpty(message="工序主键不能为空",groups = {QueryGroup.class})
    private String processId;
    @ApiModelProperty(value = "不良现象代码")
    @NotEmpty(message="不良现象代码不能为空",groups = {QueryGroup.class})
    private String defectCode;
    @NotNull(message="不良数量不能为空",groups = {QueryGroup.class})
    @ApiModelProperty(value = "不良数量")
    private Long qty;
}
