package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产排程工位
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesMoScheduleStation对象", description="生产排程工位")
public class MesMoScheduleStation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "排程主键")
    private String scheduleId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "行为")
    private String behaviorId;
    @ApiModelProperty(value = "前置时间")
    private Integer leadTime;
    @ApiModelProperty(value = "等待时间")
    private Integer waitingTime;
    @ApiModelProperty(value = "后置时间")
    private Integer postTime;
    @ApiModelProperty(value = "允许跳过")
    private Integer jump;
    @ApiModelProperty(value = "作业人数")
    private Integer jobPeoples;
    @ApiModelProperty(value = "标准工时")
    private Integer standardHours;
    @ApiModelProperty(value = "绩效系数")
    private BigDecimal coefficient;
    @ApiModelProperty(value = "人数管控")
    private Integer controlPeoples;
    @ApiModelProperty(value = "操作多岗位")
    private Integer controlMachines;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getBehaviorId() {
        return behaviorId;
    }
    public void setBehaviorId(String behaviorId) {
        this.behaviorId = behaviorId;
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

    public Integer getJump() {
        return jump;
    }
    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public Integer getJobPeoples() {
        return jobPeoples;
    }
    public void setJobPeoples(Integer jobPeoples) {
        this.jobPeoples = jobPeoples;
    }

    public Integer getStandardHours() {
        return standardHours;
    }
    public void setStandardHours(Integer standardHours) {
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



}