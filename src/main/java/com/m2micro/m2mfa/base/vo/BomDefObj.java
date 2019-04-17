package com.m2micro.m2mfa.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "BomDefObj", description = "料件物料清单明细")
public class BomDefObj {
    @ApiModelProperty(value = "主档主键")
    @NotEmpty(message = "主档主键不能为空", groups = {UpdateGroup.class})
    private String bomId;
    @ApiModelProperty(value = "组合项次")
    private Integer sequence;
    @ApiModelProperty(value = "元件料件编号")
    @Size(max = 50, message = "元件料件编号不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message = "元件料件编号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String partId;
    @ApiModelProperty(value = "特性码")
    @Size(max = 50, message = "特性码不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    private String distinguish;
    @ApiModelProperty(value = "生效日期", example = "2019-1-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    @ApiModelProperty(value = "失效日期", example = "2019-5-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invalidDate;
    @ApiModelProperty(value = "组成用量")
    private BigDecimal qpa;
    @ApiModelProperty(value = "发料单位")
    private String unit;
    @ApiModelProperty(value = "基数")
    private Integer cardinal;
    @ApiModelProperty(value = "损耗率")
    private BigDecimal lossRate;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "代替料否")
    private Boolean isSubstitute;
    @ApiModelProperty(value = "是否展开下阶")
    private Boolean isRank;
    @ApiModelProperty(value = "描述")
    @Size(max = 200, message = "描述不能大于200位", groups = {AddGroup.class, UpdateGroup.class})
    private String description;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getSubstitute() {
        return isSubstitute;
    }

    public void setSubstitute(Boolean substitute) {
        isSubstitute = substitute;
    }

    public Boolean getRank() {
        return isRank;
    }

    public void setRank(Boolean rank) {
        isRank = rank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
