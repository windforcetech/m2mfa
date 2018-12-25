package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工位基本档
 * @author liaotao
 * @since 2018-11-30
 */
@Entity
@ApiModel(value="BaseStation对象", description="工位基本档")
public class BaseStation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String stationId;
    @ApiModelProperty(value = "编号")
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @ApiModelProperty(value = "名称")
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "前置时间")
    private Integer leadTime;
    @ApiModelProperty(value = "等待时间")
    private Integer waitingTime;
    @ApiModelProperty(value = "后置时间")
    private Integer postTime;
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
    @ApiModelProperty(value = "岗位类型")
    private String postCategory;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @Transient
    @ApiModelProperty(value = "类型名称")
    private String postCategoryName;

    public BaseStation(@NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class}) String stationId, @NotEmpty(message = "编号不能为空", groups = {AddGroup.class, UpdateGroup.class}) String code, @NotEmpty(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class}) String name, Integer leadTime, Integer waitingTime, Integer postTime, Integer jobPeoples, BigDecimal standardHours, BigDecimal coefficient, Integer controlPeoples, Integer controlMachines, String postCategory, Integer sortCode, Boolean enabled, String description, String postCategoryName) {
        this.stationId = stationId;
        this.code = code;
        this.name = name;
        this.leadTime = leadTime;
        this.waitingTime = waitingTime;
        this.postTime = postTime;
        this.jobPeoples = jobPeoples;
        this.standardHours = standardHours;
        this.coefficient = coefficient;
        this.controlPeoples = controlPeoples;
        this.controlMachines = controlMachines;
        this.postCategory = postCategory;
        this.sortCode = sortCode;
        this.enabled = enabled;
        this.description = description;
        this.postCategoryName = postCategoryName;
    }

    public BaseStation() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
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

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Integer getPostTime() {
        return postTime;
    }

    public void setPostTime(Integer postTime) {
        this.postTime = postTime;
    }

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

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
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

    public String getPostCategoryName() {
        return postCategoryName;
    }

    public void setPostCategoryName(String postCategoryName) {
        this.postCategoryName = postCategoryName;
    }
}