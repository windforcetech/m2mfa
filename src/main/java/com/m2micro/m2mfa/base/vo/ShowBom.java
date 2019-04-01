package com.m2micro.m2mfa.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ShowBom {
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "品名(parts表)")
    private String name;
    @ApiModelProperty(value = "规格(parts表)")
    private String spec;
    @ApiModelProperty(value = "生效日期", example = "2019-1-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    @ApiModelProperty(value = "失效日期", example = "2019-5-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invalidDate;
    @ApiModelProperty(value = "组成用量(desc无)")
    private BigDecimal qpa;
    @ApiModelProperty(value = "发料单位(desc在parts表)")
    private String unit;
    @ApiModelProperty(value = "底数(desc无)")
    private Integer cardinal;
    @ApiModelProperty(value = "损耗率(desc在parts表)")
    private BigDecimal lossRate;
    @ApiModelProperty(value = "物料来源(parts表)")
    private String source;
    @ApiModelProperty(value = "单重(全无)")
    private String weight;
    @ApiModelProperty(value = "子级")
    private List<ShowBom> showBomList;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(String distinguish) {
        this.distinguish = distinguish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(Date invalidDate) {
        this.invalidDate = invalidDate;
    }

    public BigDecimal getQpa() {
        return qpa;
    }

    public void setQpa(BigDecimal qpa) {
        this.qpa = qpa;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCardinal() {
        return cardinal;
    }

    public void setCardinal(Integer cardinal) {
        this.cardinal = cardinal;
    }

    public BigDecimal getLossRate() {
        return lossRate;
    }

    public void setLossRate(BigDecimal lossRate) {
        this.lossRate = lossRate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<ShowBom> getShowBomList() {
        return showBomList;
    }

    public void setShowBomList(List<ShowBom> showBomList) {
        this.showBomList = showBomList;
    }
}
