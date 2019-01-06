package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
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
    @NotEmpty(message="排产单主键不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键")
    @Id
    private String scheduleId;

    @ApiModelProperty(value = "排产单号")
    private String scheduleNo;
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

    @ApiModelProperty(value = "计划天数")
    private Integer planDay;

    @NotEmpty(message="工单ID不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工作群组")
    private String machineId;

//    @Transient
//    @ApiModelProperty(value = "工作群组名称")
//    private String machineName;

    @NotEmpty(message="工单ID不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工单id")
    private String moId;

    @Transient
    @ApiModelProperty(value = "工单号码")
    private String moNumber;

    @NotEmpty(message="料件编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "料件编号")
    private String partId;



    @Transient
    @ApiModelProperty(value = "料件编号")
    private String partNo;


    @NotEmpty(message="版本不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "班别")
    private String shiftId;

    @Transient
    @ApiModelProperty(value = "班别信息")
    private List<MesMoScheduleShift> MesMoScheduleShifts;

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
    @ApiModelProperty(value = "冻结前状态")
    private Integer prefreezingState;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
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

    public Integer getPlanDay() {
        return planDay;
    }

    public void setPlanDay(Integer planDay) {
        this.planDay = planDay;
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

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public List<MesMoScheduleShift> getMesMoScheduleShifts() {
        return MesMoScheduleShifts;
    }

    public void setMesMoScheduleShifts(List<MesMoScheduleShift> mesMoScheduleShifts) {
        MesMoScheduleShifts = mesMoScheduleShifts;
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

    public Integer getPrefreezingState() {
        return prefreezingState;
    }

    public void setPrefreezingState(Integer prefreezingState) {
        this.prefreezingState = prefreezingState;
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