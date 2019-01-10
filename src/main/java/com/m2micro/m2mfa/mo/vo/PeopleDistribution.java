package com.m2micro.m2mfa.mo.vo;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 排产单人员分配
 */
@Data
@Builder
public class PeopleDistribution {
    @ApiModelProperty(value = "部门名称")
    private String  departmentName;

    @ApiModelProperty(value = "部门id")
    private String  departmentId;

    @ApiModelProperty(value = "机台信息")
    private List<BaseMachine> baseMachines;

}
