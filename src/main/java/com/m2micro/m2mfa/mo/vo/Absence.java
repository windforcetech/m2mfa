package com.m2micro.m2mfa.mo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Absence {
    @ApiModelProperty(value = "机台编号")
    private String machineCode;
    @ApiModelProperty(value = "排产Id")
    private String scheduleId;
    @ApiModelProperty(value = "排产单号")
    private String  scheduleNo;
    @ApiModelProperty(value = "物料编号")
    private String partNo;
    @ApiModelProperty(value = "工序")
    private String processName;
    @ApiModelProperty(value = "工位")
    private String stationName;
    @ApiModelProperty(value = "日期")
    private Date createTime;
    @ApiModelProperty(value = "班别")
    private String shiftName;
}
