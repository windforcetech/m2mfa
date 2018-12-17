package com.m2micro.m2mfa.pr.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 料件途程设定工位
 * @author chenshuhong
 * @since 2018-12-17
 */
@Entity
@ApiModel(value="MesPartRouteStation对象", description="料件途程设定工位")
public class MesPartRouteStation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "途程主键")
    private String partRouteId;
    @ApiModelProperty(value = "工序主键")
    private String processId;
    @ApiModelProperty(value = "允许跳过")
    private Integer jump;
    @ApiModelProperty(value = "工位主键")
    private String stationId;
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
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPartRouteId() {
        return partRouteId;
    }
    public void setPartRouteId(String partRouteId) {
        this.partRouteId = partRouteId;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Integer getJump() {
        return jump;
    }
    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
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

    public Integer getEnabled() {
        return enabled;
    }
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}