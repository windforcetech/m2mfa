package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FinishHomeworkPara", description = "结束作业输入参数")
public class FinishHomeworkPara {

    @ApiModelProperty(value = "排产单Id")
    private String scheduleId;
    @ApiModelProperty(value = "工位Id")
    private String stationId;
    @ApiModelProperty(value = "工序主键")
    private String processId;
    @ApiModelProperty(value = "上工记录主键")
    private String rwid;
    @ApiModelProperty(value = "人员作业记录主键")
    private String recordStaffId;

}
