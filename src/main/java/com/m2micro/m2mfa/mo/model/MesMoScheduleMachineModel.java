package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 14:47
 * @Description:
 */
@ApiModel(description="排产机台信息")
@Data
public class MesMoScheduleMachineModel {
    @ApiModelProperty(value = "机台id")
    private String machineId;
    @ApiModelProperty(value = "机台编号")
    private String code;
    @ApiModelProperty(value = "机台名称")
    private String name;
    @ApiModelProperty(value = "所属部门")
    private String departmentName;
    @ApiModelProperty(value = "状态")
    private String flagName;
}
