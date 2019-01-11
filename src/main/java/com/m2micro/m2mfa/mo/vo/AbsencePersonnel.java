package com.m2micro.m2mfa.mo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AbsencePersonnel {
    @ApiModelProperty(value = "缺勤人员ID")
    private String lackstaffId;
    @ApiModelProperty(value = "替补人员ID")
    private String forstaffId;
    @ApiModelProperty(value = "排产单Id")
    private String[] scheduleIds;
}
