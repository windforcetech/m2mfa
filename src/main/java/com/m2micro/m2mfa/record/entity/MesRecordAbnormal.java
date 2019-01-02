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
 * 异常记录表
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordAbnormal对象", description="异常记录表")
public class MesRecordAbnormal implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工索引")
    private String rwId;
    @ApiModelProperty(value = "异常项目id")
    private String abnormalId;
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
    @ApiModelProperty(value = "产生异常单")
    private Boolean isReceipt;
    @ApiModelProperty(value = "异常提报职员")
    private String startStaff;
    @ApiModelProperty(value = "异常解除职员")
    private String endStaff;

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

    public String getAbnormalId() {
        return abnormalId;
    }
    public void setAbnormalId(String abnormalId) {
        this.abnormalId = abnormalId;
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

    public Boolean getReceipt() {
        return isReceipt;
    }
    public void setReceipt(Boolean isReceipt) {
        this.isReceipt = isReceipt;
    }

    public String getStartStaff() {
        return startStaff;
    }
    public void setStartStaff(String startStaff) {
        this.startStaff = startStaff;
    }

    public String getEndStaff() {
        return endStaff;
    }
    public void setEndStaff(String endStaff) {
        this.endStaff = endStaff;
    }



}