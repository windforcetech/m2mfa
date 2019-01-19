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
    @ApiModelProperty(value = "上工标志位/下工标志位(0:上工,1:下工)")
    private String workFlag;
    @ApiModelProperty(value = "上下工(0:置灰,1:不置灰)")
    private String startWork;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "上工记录主键")
    private String rwid;
    @ApiModelProperty(value = "人员作业记录主键")
    private String recordStaffId;
    @ApiModelProperty(value = "不良品数(0:置灰,1:不置灰)")
    private String defectiveProducts;
    @ApiModelProperty(value = "提报异常(0:置灰,1:不置灰)")
    private String reportingAnomalies;
    @ApiModelProperty(value = "异常主键")
    private String recordAbnormalId;
    @ApiModelProperty(value = "作业输入(0:置灰,1:不置灰)")
    private String jobInput;
    @ApiModelProperty(value = "作业指导(0:置灰,1:不置灰)")
    private String homeworkGuidance;
    @ApiModelProperty(value = "操作历史(0:置灰,1:不置灰)")
    private String operationHistory;
    @ApiModelProperty(value = "结束作业(0:置灰,1:不置灰)")
    private String finishHomework;





    public OperationInfo() {
        super();
    }

}
