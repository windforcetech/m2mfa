package com.m2micro.m2mfa.mo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessStatus {

    @ApiModelProperty(value = "排产单ID")
    private String scheduleId;
    @ApiModelProperty(value = "工序ID")
    private String processId;
}
