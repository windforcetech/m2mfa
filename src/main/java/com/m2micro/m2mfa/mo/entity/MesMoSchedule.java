package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产排程表表头
 * @author liaotao
 * @since 2018-12-26
 */
@Entity
@ApiModel(value="MesMoSchedule对象", description="生产排程表表头")
public class MesMoSchedule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String scheduleId;
    @ApiModelProperty(value = "排程日期")
    private Date scheduleDate;
    @ApiModelProperty(value = "版本号")
    private String revsion;
    @ApiModelProperty(value = "来源")
    private Integer source;
    @ApiModelProperty(value = "任务主键")
    private String jobid;
    @ApiModelProperty(value = "产线主键")
    private Integer lineId;
    @ApiModelProperty(value = "工作群组")
    private String machineId;
    @ApiModelProperty(value = "工单id")
    private String moId;
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "班别")
    private String shiftId;
    @ApiModelProperty(value = "生产顺序")
    private Integer sequence;
    @ApiModelProperty(value = "预计开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "预计结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "排产数量")
    private Integer scheduleQty;
    @ApiModelProperty(value = "实际开始时间")
    private Date actualStartTime;
    @ApiModelProperty(value = "实际结束时间")
    private Date actualEndTime;
    @ApiModelProperty(value = "是否激活")
    private Integer isActivated;
    @ApiModelProperty(value = "审核")
    private Integer checkFlag;
    @ApiModelProperty(value = "审核日期")
    private Date checkOn;
    @ApiModelProperty(value = "审核用户主键")
    private String checkBy;
    @ApiModelProperty(value = "生产状态")
    private Integer flag;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getRevsion() {
        return revsion;
    }
    public void setRevsion(String revsion) {
        this.revsion = revsion;
    }

    public Integer getSource() {
        return source;
    }
    public void setSource(Integer source) {
        this.source = source;
    }

    public String getJobid() {
        return jobid;
    }
    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public Integer getLineId() {
        return lineId;
    }
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMoId() {
        return moId;
    }
    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getShiftId() {
        return shiftId;
    }
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Integer getScheduleQty() {
        return scheduleQty;
    }
    public void setScheduleQty(Integer scheduleQty) {
        this.scheduleQty = scheduleQty;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }
    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }
    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Integer getIsActivated() {
        return isActivated;
    }
    public void setIsActivated(Integer isActivated) {
        this.isActivated = isActivated;
    }

    public Integer getCheckFlag() {
        return checkFlag;
    }
    public void setCheckFlag(Integer checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Date getCheckOn() {
        return checkOn;
    }
    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getCheckBy() {
        return checkBy;
    }
    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
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