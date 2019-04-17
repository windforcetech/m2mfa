package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("包装信息")
public class PackObj {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "净重")
    private BigDecimal nw;
    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;
    @ApiModelProperty(value = "材积")
    private BigDecimal cuft;
    @ApiModelProperty(value = "容量")
    private BigDecimal qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getNw() {
        return nw;
    }

    public void setNw(BigDecimal nw) {
        this.nw = nw;
    }

    public BigDecimal getGw() {
        return gw;
    }

    public void setGw(BigDecimal gw) {
        this.gw = gw;
    }

    public BigDecimal getCuft() {
        return cuft;
    }

    public void setCuft(BigDecimal cuft) {
        this.cuft = cuft;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
