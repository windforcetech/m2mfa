package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @Auther: liaotao
 * @Date: 2019/1/6 10:44
 * @Description:
 */
@ApiModel(description="排产单顺序信息")
@Data
public class ScheduleSequenceModel {
    @ApiModelProperty(value = "主键")
    private String scheduleId;
    @ApiModelProperty(value = "生产顺序")
    private Integer sequence;
}
