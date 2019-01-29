package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 检验项目
 * @author liaotao
 * @since 2019-01-28
 */
@Entity
@ApiModel(value="BaseQualityItems对象", description="检验项目")
public class BaseQualityItems extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String itemId;
    @ApiModelProperty(value = "编号")
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String itemCode;
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称")
    private String itemName;
    @ApiModelProperty(value = "量具名称")
    private String gauge;
    @Transient
    @ApiModelProperty(value = "量具名称name")
    private String gaugeName;
    @ApiModelProperty(value = "检验值类型")
    private String category;
    @Transient
    @ApiModelProperty(value = "检验值类型名称")
    private String categoryName;
    @ApiModelProperty(value = "上限值")
    private BigDecimal upperLimit;
    @ApiModelProperty(value = "下限值")
    private BigDecimal lowerLimit;
    @ApiModelProperty(value = "中心值")
    private BigDecimal centralLimit;
    @ApiModelProperty(value = "检验单位")
    private String limitUnit;
    @Transient
    @ApiModelProperty(value = "检验单位名称")
    private String limitUnitName;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getGaugeName() {
        return gaugeName;
    }

    public void setGaugeName(String gaugeName) {
        this.gaugeName = gaugeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLimitUnitName() {
        return limitUnitName;
    }

    public void setLimitUnitName(String limitUnitName) {
        this.limitUnitName = limitUnitName;
    }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGauge() {
        return gauge;
    }
    public void setGauge(String gauge) {
        this.gauge = gauge;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }
    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }
    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public BigDecimal getCentralLimit() {
        return centralLimit;
    }
    public void setCentralLimit(BigDecimal centralLimit) {
        this.centralLimit = centralLimit;
    }

    public String getLimitUnit() {
        return limitUnit;
    }
    public void setLimitUnit(String limitUnit) {
        this.limitUnit = limitUnit;
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