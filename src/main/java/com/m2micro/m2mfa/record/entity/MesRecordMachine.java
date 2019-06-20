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
 * 机台抄读历史记录
 * @author chenshuhong
 * @since 2019-06-19
 */
@Entity
@ApiModel(value="MesRecordMachine对象", description="机台抄读历史记录")
public class MesRecordMachine implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "机台")
    private String machineId;
    @ApiModelProperty(value = "排产单id")
    private String scheduleId;
    @ApiModelProperty(value = "料件ID")
    private String partId;
    @ApiModelProperty(value = "模具id")
    private String moldId;
    @ApiModelProperty(value = "模具穴数")
    private Integer cavityQty;
    @ApiModelProperty(value = "生产穴数")
    private Integer cavityAvailable;
    @ApiModelProperty(value = "开模次数")
    private Integer openQty;
    @ApiModelProperty(value = "电量")
    private BigDecimal power;
    @ApiModelProperty(value = "班别")
    private String shiftId;
    @ApiModelProperty(value = "操作员id")
    private String staffId;
    @ApiModelProperty(value = "创建时间")
    private Date createOn;

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public Integer getCavityQty() {
        return cavityQty;
    }
    public void setCavityQty(Integer cavityQty) {
        this.cavityQty = cavityQty;
    }

    public Integer getCavityAvailable() {
        return cavityAvailable;
    }
    public void setCavityAvailable(Integer cavityAvailable) {
        this.cavityAvailable = cavityAvailable;
    }

    public Integer getOpenQty() {
        return openQty;
    }
    public void setOpenQty(Integer openQty) {
        this.openQty = openQty;
    }

    public BigDecimal getPower() {
        return power;
    }
    public void setPower(BigDecimal power) {
        this.power = power;
    }

    public String getShiftId() {
        return shiftId;
    }
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }



}