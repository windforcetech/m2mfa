package com.m2micro.m2mfa.pad.model;


import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "CrossStationProcess", description = "过站工序信息")
public class CrossStationProcess {
    @ApiModelProperty(value = "工序id")
    private String processId;
    @ApiModelProperty(value = "工序名称")
    private String processName;
}
