package com.m2micro.m2mfa.report.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author chenshuhong
 * @since 2019-06-10
 */
@Entity
@ApiModel(value="MesStaffInfo对象", description="")
public class MesStaffInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String mesStaffInfoId;
    @ApiModelProperty(value = "机台")
    private String machineId;
    @ApiModelProperty(value = "人员")
    private String staffId;
    @ApiModelProperty(value = "工单")
    private String moId;
    @ApiModelProperty(value = "排单")
    private String scheduleId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "料件")
    private String partId;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "状态")
    private Integer flag;
    @ApiModelProperty(value = "产量")
    private BigDecimal outputQty;
    @ApiModelProperty(value = "不良")
    private BigDecimal failQty;
    @ApiModelProperty(value = "报废")
    private BigDecimal scrapQty;
    @ApiModelProperty(value = "模具")
    private String moldId;
    @ApiModelProperty(value = "模数")
    private BigDecimal molds;
    @ApiModelProperty(value = "穴数")
    private Integer cavityAvailable;
    @ApiModelProperty(value = "标准生产率")
    private BigDecimal standardHours;

    public String getMesStaffInfoId() {
        return mesStaffInfoId;
    }
    public void setMesStaffInfoId(String mesStaffInfoId) {
        this.mesStaffInfoId = mesStaffInfoId;
    }

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getMoId() {
        return moId;
    }
    public void setMoId(String moId) {
        this.moId = moId;
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

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
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

    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public BigDecimal getOutputQty() {
        return outputQty;
    }
    public void setOutputQty(BigDecimal outputQty) {
        this.outputQty = outputQty;
    }

    public BigDecimal getFailQty() {
        return failQty;
    }
    public void setFailQty(BigDecimal failQty) {
        this.failQty = failQty;
    }

    public BigDecimal getScrapQty() {
        return scrapQty;
    }
    public void setScrapQty(BigDecimal scrapQty) {
        this.scrapQty = scrapQty;
    }

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public BigDecimal getMolds() {
        return molds;
    }
    public void setMolds(BigDecimal molds) {
        this.molds = molds;
    }

    public Integer getCavityAvailable() {
        return cavityAvailable;
    }
    public void setCavityAvailable(Integer cavityAvailable) {
        this.cavityAvailable = cavityAvailable;
    }

    public BigDecimal getStandardHours() {
        return standardHours;
    }
    public void setStandardHours(BigDecimal standardHours) {
        this.standardHours = standardHours;
    }



}
