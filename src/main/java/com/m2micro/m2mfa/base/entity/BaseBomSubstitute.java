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
 * 料件物料清单
 *
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value = "BaseBomSubstitute对象", description = "料件物料清单取替代关联")
public class BaseBomSubstitute extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class})
    private String id;
    @ApiModelProperty(value = "主档主键")
    @NotEmpty(message = "主档主键不能为空", groups = {UpdateGroup.class})
    private String bomId;
    @ApiModelProperty(value = "料件编号id")
    private String partId;
    @ApiModelProperty(value = "替代料件编号id")
    private String subPartId;
    @Size(max = 50, message = "类型不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "替代类型")
    private String category;
    @ApiModelProperty(value = "替代序号")
    private Integer sequence;
    @ApiModelProperty(value = "生效日期", example = "2019-1-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    @ApiModelProperty(value = "失效日期", example = "2019-5-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invalidDate;
    @ApiModelProperty(value = "代替率(替代料用量/被替代料用量)")
    private BigDecimal substituteRate;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    @Size(max = 200, message = "描述不能大于200位", groups = {AddGroup.class, UpdateGroup.class})
    private String description;


    @ApiModelProperty(value = "料件编号")
    @Transient
    private String partNo;


    @ApiModelProperty(value = "替代料件编号")
    @Transient
    private String subPartNo;


    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }
    public String getSubPartNo() {
        return subPartNo;
    }

    public void setSubPartNo(String subPartNo) {
        this.subPartNo = partNo;
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

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getSubPartId() {
        return subPartId;
    }

    public void setSubPartId(String subPartId) {
        this.subPartId = subPartId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public BigDecimal getSubstituteRate() {
        return substituteRate;
    }

    public void setSubstituteRate(BigDecimal substituteRate) {
        this.substituteRate = substituteRate;
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