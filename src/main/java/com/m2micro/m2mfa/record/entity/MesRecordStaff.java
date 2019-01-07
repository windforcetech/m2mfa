package com.m2micro.m2mfa.record.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人员作业记录
 * @author wanglei
 * @since 2018-12-27
 */
@Entity
@ApiModel(value="MesRecordStaff对象", description="人员作业记录")
public class MesRecordStaff implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "排产单id")
    private String scheduleId;
    @ApiModelProperty(value = "上工索引")
    private String rwId;
    @ApiModelProperty(value = "员工id")
    private String staffId;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "开始电量")
    private BigDecimal stratPower;
    @ApiModelProperty(value = "结束电量")
    private BigDecimal endPower;
    @ApiModelProperty(value = "开始模数")
    private BigDecimal startMolds;
    @ApiModelProperty(value = "结束模数")
    private BigDecimal endMolds;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getRwId() {
        return rwId;
    }
    public void setRwId(String rwId) {
        this.rwId = rwId;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStratPower() {
        return stratPower;
    }
    public void setStratPower(BigDecimal stratPower) {
        this.stratPower = stratPower;
    }

    public BigDecimal getEndPower() {
        return endPower;
    }
    public void setEndPower(BigDecimal endPower) {
        this.endPower = endPower;
    }

    public BigDecimal getStartMolds() {
        return startMolds;
    }
    public void setStartMolds(BigDecimal startMolds) {
        this.startMolds = startMolds;
    }

    public BigDecimal getEndMolds() {
        return endMolds;
    }
    public void setEndMolds(BigDecimal endMolds) {
        this.endMolds = endMolds;
    }



}