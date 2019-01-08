package com.m2micro.m2mfa.iot.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    @ApiModelProperty(value = "机台id")
    private String machineId;
    @ApiModelProperty(value = "电量")
    private BigDecimal power;
    @ApiModelProperty(value = "模数")
    private BigDecimal molds;

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



}