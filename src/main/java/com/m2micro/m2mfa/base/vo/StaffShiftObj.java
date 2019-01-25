package com.m2micro.m2mfa.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "StaffShiftObj", description = "排班规则定义")
public class StaffShiftObj {
    @ApiModelProperty(value = "部门主键")
    private String departmentId;
    @ApiModelProperty(value = "员工主键,逗号分隔，可以为空")
    private String staffId;
    @ApiModelProperty(value = "开始日期",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @ApiModelProperty(value = "结束日期",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @ApiModelProperty(value = "过滤日期,逗号分隔，可以为空",example = "2018-11-21")
    private String excludeDate;
    @ApiModelProperty(value = "过滤员工主键,逗号分隔，可以为空",example = "2018-11-21")
    private String excludeStaffId;
    @ApiModelProperty(value = "班别代码")
    private String shiftId;
    @ApiModelProperty(value = "强制更新")
    private Boolean forceUpdate;


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getExcludeDate() {
        return excludeDate;
    }

    public void setExcludeDate(String excludeDate) {
        this.excludeDate = excludeDate;
    }

    public String getExcludeStaffId() {
        return excludeStaffId;
    }

    public void setExcludeStaffId(String excludeStaffId) {
        this.excludeStaffId = excludeStaffId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

}
