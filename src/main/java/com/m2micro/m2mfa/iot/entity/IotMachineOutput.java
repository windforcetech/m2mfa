package com.m2micro.m2mfa.iot.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 机台产出信息
 * @author liaotao
 * @since 2019-01-08
 */
@Entity
@ApiModel(value="IotMachineOutput对象", description="机台产出信息")
public class IotMachineOutput implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "物业id")
    private String orgId;
    @ApiModelProperty(value = "电量")
    private BigDecimal power;
    @ApiModelProperty(value = "模数")
    private BigDecimal molds;
    @ApiModelProperty(value = "产量")
    private BigDecimal output;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @ApiModelProperty(value = "创建日期",example = "2018-11-21 12:00:00")
    private Date createOn;
    @ApiModelProperty(value = "修改日期",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private Date modifiedOn;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getPower() {
        return power;
    }
    public void setPower(BigDecimal power) {
        this.power = power;
    }

    public BigDecimal getMolds() {
        return molds;
    }
    public void setMolds(BigDecimal molds) {
        this.molds = molds;
    }

    public BigDecimal getOutput() {
        return output;
    }

    public void setOutput(BigDecimal output) {
        this.output = output;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}