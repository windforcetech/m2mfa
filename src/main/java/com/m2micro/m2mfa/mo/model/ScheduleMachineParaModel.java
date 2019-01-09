package com.m2micro.m2mfa.mo.model;

import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.QueryGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 15:48
 * @Description:
 */
@ApiModel(description="排产机台变更参数")
@Data
public class ScheduleMachineParaModel {
    @ApiModelProperty(value = "原机台id(左侧树选中的机台id)",required = true)
    @NotEmpty(message="原机台id不能为空",groups = {QueryGroup.class})
    private String oldMachineId;
    @ApiModelProperty(value = "新机台id(变更后的机台id)",required = true)
    @NotEmpty(message="新机台id不能为空",groups = {QueryGroup.class})
    private String newMachineId;
    @ApiModelProperty(value = "需要变更的排产id(选中的排产单)",required = true)
    @Size(min=1,message = "请选择排产单",groups = {QueryGroup.class})
    @NotNull(message="请选择排产单",groups = {QueryGroup.class})
    private List<String> scheduleIds;



}
