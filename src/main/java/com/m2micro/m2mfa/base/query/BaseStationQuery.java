package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description="工位基本档查询参数")
public class BaseStationQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "岗位类型")
    private String postCategory;
    @ApiModelProperty(value = "作业人数")
    private Integer jobPeoples;
    @ApiModelProperty(value = "标准工时")
    private BigDecimal standardHours;
    @ApiModelProperty(value = "绩效系数")
    private BigDecimal coefficient;
    @ApiModelProperty(value = "作业人员管制")
    private Integer controlPeoples;
    @ApiModelProperty(value = "一人操多机管制")
    private Integer controlMachines;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public Integer getJobPeoples() {
        return jobPeoples;
    }

    public void setJobPeoples(Integer jobPeoples) {
        this.jobPeoples = jobPeoples;
    }

    public BigDecimal getStandardHours() {
        return standardHours;
    }

    public void setStandardHours(BigDecimal standardHours) {
        this.standardHours = standardHours;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getControlPeoples() {
        return controlPeoples;
    }

    public void setControlPeoples(Integer controlPeoples) {
        this.controlPeoples = controlPeoples;
    }

    public Integer getControlMachines() {
        return controlMachines;
    }

    public void setControlMachines(Integer controlMachines) {
        this.controlMachines = controlMachines;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }
}
