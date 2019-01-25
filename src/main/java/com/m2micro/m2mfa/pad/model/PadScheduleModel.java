package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2019/1/15 17:23
 * @Description:
 */
@ApiModel(description="pad端登陆后排产信息")
@Data
public class PadScheduleModel {
    @ApiModelProperty(value = "排产单主键")
    private String scheduleId;
    @ApiModelProperty(value = "排产单编号")
    private String scheduleNo;
    @ApiModelProperty(value = "生产顺序")
    private Integer sequence;
    @ApiModelProperty(value = "机台ID")
    private String machineId;
    @ApiModelProperty(value = "机台编号")
    private String machineCode;
    @ApiModelProperty(value = "机台名称")
    private String machineName;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "排产单状态")
    private String flagStatus;
}
