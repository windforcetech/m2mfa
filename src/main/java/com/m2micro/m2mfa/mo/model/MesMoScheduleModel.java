package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * @Auther: liaotao
 * @Date: 2019/1/3 15:24
 * @Description:
 */
@ApiModel(description="排产单综合信息")
@Data
public class MesMoScheduleModel {
    @ApiModelProperty(value = "主键")
    private String scheduleId;
    @ApiModelProperty(value = "排产单号")
    private String scheduleNo;
    @ApiModelProperty(value = "生产状态")
    private Integer flag;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "目标量")
    private Integer scheduleQty;
    @ApiModelProperty(value = "排产数量")
    private Integer outputQty;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "作业机台")
    private String machineName;
    @ApiModelProperty(value = "生产顺序")
    private Integer sequence;
}
