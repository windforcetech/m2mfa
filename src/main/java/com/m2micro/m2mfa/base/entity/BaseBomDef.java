package com.m2micro.m2mfa.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 料件物料清单明细
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value = "BaseBomDef对象", description = "料件物料清单明细")
public class BaseBomDef extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class})
    private String id;
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

    @ApiModelProperty(value = "(通过料件编号关联)desc明细")
    @Transient
    private List<BaseBomDesc> bomDescObjList;


    @ApiModelProperty(value = "品名(parts表)")
    @Transient
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseBomDesc> getBomDescObjList() {
        return bomDescObjList;
    }

    public void setBomDescObjList(List<BaseBomDesc> bomDescObjList) {
        this.bomDescObjList = bomDescObjList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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