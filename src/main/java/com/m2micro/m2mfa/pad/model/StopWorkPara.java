package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.record.entity.MesRecordFail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/19 15:38
 * @Description:
 */
@ApiModel(description="pad 下工参数")
@Data
public class StopWorkPara {
    @ApiModelProperty(value = "排产单Id")
    private String scheduleId;
    @ApiModelProperty(value = "工位Id")
    private String stationId;
    @ApiModelProperty(value = "上工记录主键")
    private String rwid;
    @ApiModelProperty(value = "人员作业记录主键")
    private String recordStaffId;
    @ApiModelProperty(value = "不良输入记录")
    private MesRecordFail mesRecordFail;
}
