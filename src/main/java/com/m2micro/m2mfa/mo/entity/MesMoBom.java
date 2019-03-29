package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工单料表
 * @author liaotao
 * @since 2019-03-29
 */
@Entity
@ApiModel(value="MesMoBom对象", description="工单料表")
public class MesMoBom extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "工单id")
    @Id
    private String moId;
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "发料料号")
    private String sentPartId;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "用量")
    private BigDecimal qpa;
    @ApiModelProperty(value = "基数")
    private BigDecimal cardinal;
    @ApiModelProperty(value = "应发数量")
    private BigDecimal shouldQty;
    @ApiModelProperty(value = "已发数量")
    private BigDecimal alreadyQty;
    @ApiModelProperty(value = "尚欠数量")
    private BigDecimal lackQty;
    @ApiModelProperty(value = "应退数量")
    private BigDecimal returnQty;
    @ApiModelProperty(value = "已退数量")
    private BigDecimal returnedQty;
    @ApiModelProperty(value = "超领数量")
    private BigDecimal exceedQty;
    @ApiModelProperty(value = "是否替代料")
    private Boolean isSubstitute;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMoId() {
        return moId;
    }
    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getSentPartId() {
        return sentPartId;
    }
    public void setSentPartId(String sentPartId) {
        this.sentPartId = sentPartId;
    }

    public String getPartName() {
        return partName;
    }
    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQpa() {
        return qpa;
    }
    public void setQpa(BigDecimal qpa) {
        this.qpa = qpa;
    }

    public BigDecimal getCardinal() {
        return cardinal;
    }
    public void setCardinal(BigDecimal cardinal) {
        this.cardinal = cardinal;
    }

    public BigDecimal getShouldQty() {
        return shouldQty;
    }
    public void setShouldQty(BigDecimal shouldQty) {
        this.shouldQty = shouldQty;
    }

    public BigDecimal getAlreadyQty() {
        return alreadyQty;
    }
    public void setAlreadyQty(BigDecimal alreadyQty) {
        this.alreadyQty = alreadyQty;
    }

    public BigDecimal getLackQty() {
        return lackQty;
    }
    public void setLackQty(BigDecimal lackQty) {
        this.lackQty = lackQty;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }
    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getReturnedQty() {
        return returnedQty;
    }
    public void setReturnedQty(BigDecimal returnedQty) {
        this.returnedQty = returnedQty;
    }

    public BigDecimal getExceedQty() {
        return exceedQty;
    }
    public void setExceedQty(BigDecimal exceedQty) {
        this.exceedQty = exceedQty;
    }

    public Boolean getSubstitute() {
        return isSubstitute;
    }
    public void setSubstitute(Boolean isSubstitute) {
        this.isSubstitute = isSubstitute;
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



}
