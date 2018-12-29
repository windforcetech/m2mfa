package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: liaotao
 * @Date: 2018/12/27 14:50
 * @Description:
 */
@ApiModel(description="工单主档综合信息")
@Data
public class OperationInfo {
    @ApiModelProperty(value = "上下工标志位")
    private String workFlag;
    @ApiModelProperty(value = "人员作业记录主键")
    private String recordStaffId;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "提报异常标志位")
    private String abnormalFlag;
    @ApiModelProperty(value = "异常主键")
    private String recordAbnormalId;
    @ApiModelProperty(value = "异常项目id")
    private String abnormalId;
    @ApiModelProperty(value = "异常名称")
    private String abnormalName;

    public OperationInfo() {
        super();
    }

}
