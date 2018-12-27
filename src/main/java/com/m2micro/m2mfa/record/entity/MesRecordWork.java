package com.m2micro.m2mfa.record.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 上工记录表
 * @author liaotao
 * @since 2018-12-26
 */
@Entity
@ApiModel(value="MesRecordWork对象", description="上工记录表")
public class MesRecordWork implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String rwid;
    @ApiModelProperty(value = "排产主键")
    private String scheduleId;
    @ApiModelProperty(value = "线别")
    private String lineId;
    @ApiModelProperty(value = "作业群组")
    private String workGroupId;
    @ApiModelProperty(value = "日期")
    private String dateid;
    @ApiModelProperty(value = "料号")
    private String partNo;
    @ApiModelProperty(value = "工单")
    private String moNumber;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "机台")
    private String machineId;
    @ApiModelProperty(value = "模具索引")
    private String moldId;
    @ApiModelProperty(value = "模仁索引")
    private String mandrelId;
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
    @ApiModelProperty(value = "有效工时")
    private BigDecimal effectiveHours;
    @ApiModelProperty(value = "异常工时")
    private BigDecimal invalidHours;
    @ApiModelProperty(value = "有效电量")
    private BigDecimal effectivePower;
    @ApiModelProperty(value = "异常电量")
    private BigDecimal invalidPower;
    @ApiModelProperty(value = "有效模数")
    private Integer effectiveMolds;
    @ApiModelProperty(value = "无效模数")
    private Integer invalidMolds;
    @ApiModelProperty(value = "不良数量")
    private Integer fails;
    @ApiModelProperty(value = "报废数量")
    private Integer scraps;

    public String getRwid() {
        return rwid;
    }
    public void setRwid(String rwid) {
        this.rwid = rwid;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getLineId() {
        return lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getWorkGroupId() {
        return workGroupId;
    }
    public void setWorkGroupId(String workGroupId) {
        this.workGroupId = workGroupId;
    }

    public String getDateid() {
        return dateid;
    }
    public void setDateid(String dateid) {
        this.dateid = dateid;
    }

    public String getPartNo() {
        return partNo;
    }
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getMoNumber() {
        return moNumber;
    }
    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
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

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMandrelId() {
        return mandrelId;
    }
    public void setMandrelId(String mandrelId) {
        this.mandrelId = mandrelId;
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

    public BigDecimal getEffectiveHours() {
        return effectiveHours;
    }
    public void setEffectiveHours(BigDecimal effectiveHours) {
        this.effectiveHours = effectiveHours;
    }

    public BigDecimal getInvalidHours() {
        return invalidHours;
    }
    public void setInvalidHours(BigDecimal invalidHours) {
        this.invalidHours = invalidHours;
    }

    public BigDecimal getEffectivePower() {
        return effectivePower;
    }
    public void setEffectivePower(BigDecimal effectivePower) {
        this.effectivePower = effectivePower;
    }

    public BigDecimal getInvalidPower() {
        return invalidPower;
    }
    public void setInvalidPower(BigDecimal invalidPower) {
        this.invalidPower = invalidPower;
    }

    public Integer getEffectiveMolds() {
        return effectiveMolds;
    }
    public void setEffectiveMolds(Integer effectiveMolds) {
        this.effectiveMolds = effectiveMolds;
    }

    public Integer getInvalidMolds() {
        return invalidMolds;
    }
    public void setInvalidMolds(Integer invalidMolds) {
        this.invalidMolds = invalidMolds;
    }

    public Integer getFails() {
        return fails;
    }
    public void setFails(Integer fails) {
        this.fails = fails;
    }

    public Integer getScraps() {
        return scraps;
    }
    public void setScraps(Integer scraps) {
        this.scraps = scraps;
    }



}