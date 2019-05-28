package com.m2micro.m2mfa.mo.query;

import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @Auther: liaotao
 * @Date: 2018/12/10 10:01
 * @Description:
 */
@ApiModel(description = "工单主档查询参数")
public class MesMoDescQuery extends Query {
    @ApiModelProperty(value = "工单号码")
    private String moNumber;
    @ApiModelProperty(value = "工单状态")
    private String closeFlag;

    @ApiModelProperty(value = "工单类型")
    private String category;

    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "目标量")
    private Integer targetQty;
    @ApiModelProperty(value = "达交时间")
    private Date reachDate;
    @ApiModelProperty(value = "预排机台数")
    private Integer machineQty;
    @ApiModelProperty(value = "客户主键")
    private String customerName;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "工单版本")
    private Integer revsion;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "bom版本")
    private Integer bomRevsion;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public Integer getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(Integer targetQty) {
        this.targetQty = targetQty;
    }



    public Date getReachDate() {
        return reachDate;
    }

    public void setReachDate(Date reachDate) {
        this.reachDate = reachDate;
    }

    public Integer getMachineQty() {
        return machineQty;
    }

    public void setMachineQty(Integer machineQty) {
        this.machineQty = machineQty;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Integer getRevsion() {
        return revsion;
    }

    public void setRevsion(Integer revsion) {
        this.revsion = revsion;
    }

    public String getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(String distinguish) {
        this.distinguish = distinguish;
    }

    public Integer getBomRevsion() {
        return bomRevsion;
    }

    public void setBomRevsion(Integer bomRevsion) {
        this.bomRevsion = bomRevsion;
    }

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(String closeFlag) {
        this.closeFlag = closeFlag;
    }
}
