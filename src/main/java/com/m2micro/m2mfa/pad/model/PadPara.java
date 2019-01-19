package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/14 17:34
 * @Description:
 */
@Data
@ApiModel(value = "padPara", description = "Pad参数")
public class PadPara {
    @ApiModelProperty(value = "工位Id")
    private String stationId;
    @ApiModelProperty(value = "排产单Id")
    private String scheduleId;
    @ApiModelProperty(value = "工序Id")
    private String processId;
}
