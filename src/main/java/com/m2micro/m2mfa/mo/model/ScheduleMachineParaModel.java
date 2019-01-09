package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 15:48
 * @Description:
 */
@ApiModel(description="排产机台变更参数")
@Data
public class ScheduleMachineParaModel {
    @ApiModelProperty(value = "原机台id(左侧树选中的机台id)")
    private String oldMachineId;
    @ApiModelProperty(value = "新机台id(变更后的机台id)")
    private String newMachineId;
    @ApiModelProperty(value = "需要变更的排产id(选中的排产单)")
    private List<String> scheduleIds;
}
