package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

import com.m2micro.m2mfa.base.entity.Staffmember;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产排程人员
 * @author liaotao
 * @since 2018-12-26
 */
@Entity
@ApiModel(value="MesMoScheduleStaff对象", description="生产排程人员")
public class MesMoScheduleStaff extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotEmpty(message="生产排程人员主键不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @NotEmpty(message="排称主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "排程主键")
    private String scheduleId;
    @NotEmpty(message="工序不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工序")
    private String processId;
    @NotEmpty(message="工位主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工位主键")
    private String stationId;
   // @NotEmpty(message="员工ID不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "员工id")
    private String staffId;
  //  @NotEmpty(message="班别ID不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "班别id")
    private String shiftId;
    @ApiModelProperty(value = "预计开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "预计结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "实际开始时间")
    private Date actualStartTime;
    @ApiModelProperty(value = "实际结束时间")
    private Date actualEndTime;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "是否岗位")
    private Boolean isStation;
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "工位人员信息")
    @Transient
    private Staffmember staffmember;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsStation() {
        return isStation;
    }

    public void setIsStation(Boolean station) {
        isStation = station;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Staffmember getStaffmember() {
        return staffmember;
    }

    public void setStaffmember(Staffmember staffmember) {
        this.staffmember = staffmember;
    }
}