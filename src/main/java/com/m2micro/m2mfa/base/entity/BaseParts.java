package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 料件基本资料
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value="BaseParts对象", description="料件基本资料")
public class BaseParts extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String partId;
    @ApiModelProperty(value = "料件编号")
    @NotEmpty(message="料件编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String partNo;
    @ApiModelProperty(value = "品名")
    @NotEmpty(message="品名不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "规格")
    @NotEmpty(message="规格不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String spec;
    @ApiModelProperty(value = "目前版本号")
    @NotEmpty(message="目前版本号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String version;
    @ApiModelProperty(value = "物料等级")
    @Size(min=1,max=1,message="物料等级不能超过1位",groups = {AddGroup.class, UpdateGroup.class})
    private String grade;
    @ApiModelProperty(value = "物料来源")
    private String source;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "单重/净重(单位kg)")
    private BigDecimal single;
    @ApiModelProperty(value = "检验否")
    private Boolean isCheck;
    @ApiModelProperty(value = "库存单位")
    private String stockUnit;
    @ApiModelProperty(value = "安全库存量")
    private Integer safetyStock;
    @ApiModelProperty(value = "最高储存数量")
    private Integer maxStock;
    @ApiModelProperty(value = "主要仓库别")
    private String mainWarehouse;
    @ApiModelProperty(value = "主要储位别")
    private String mainStorage;
    @ApiModelProperty(value = "生产单位")
    private String productionUnit;
    @ApiModelProperty(value = "生产单位/库存单位换算率")
    private Integer productionConversionRate;
    @ApiModelProperty(value = "最少生产数量")
    private Integer minProductionQty;
    @ApiModelProperty(value = "生产损耗率")
    private BigDecimal productionLossRate;
    @ApiModelProperty(value = "发料单位")
    private String sentUnit;
    @ApiModelProperty(value = "发料单位/库存单位换算率")
    private Integer sentConversionRate;
    @ApiModelProperty(value = "最少发料数量")
    private Integer minSentQty;
    @ApiModelProperty(value = "消耗料件否")
    private Boolean isConsume;
    @ApiModelProperty(value = "储存有效天数")
    private Integer validityDays;
    @ApiModelProperty(value = "线边主要仓库别")
    private String mainLineWarehouse;
    @ApiModelProperty(value = "线边主要储位别")
    private String mainLineStorage;
    @ApiModelProperty(value = "产品正面图")
    private String positiveImageUrl;
    @ApiModelProperty(value = "产品反面图")
    private String negativeImage;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartNo() {
        return partNo;
    }
    public void setPartNo(String partNo) {
        this.partNo = partNo;
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

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getSingle() {
        return single;
    }
    public void setSingle(BigDecimal single) {
        this.single = single;
    }

    public Boolean getCheck() {
        return isCheck;
    }
    public void setCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getStockUnit() {
        return stockUnit;
    }
    public void setStockUnit(String stockUnit) {
        this.stockUnit = stockUnit;
    }

    public Integer getSafetyStock() {
        return safetyStock;
    }
    public void setSafetyStock(Integer safetyStock) {
        this.safetyStock = safetyStock;
    }

    public Integer getMaxStock() {
        return maxStock;
    }
    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public String getMainWarehouse() {
        return mainWarehouse;
    }
    public void setMainWarehouse(String mainWarehouse) {
        this.mainWarehouse = mainWarehouse;
    }

    public String getMainStorage() {
        return mainStorage;
    }
    public void setMainStorage(String mainStorage) {
        this.mainStorage = mainStorage;
    }

    public String getProductionUnit() {
        return productionUnit;
    }
    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    public Integer getProductionConversionRate() {
        return productionConversionRate;
    }
    public void setProductionConversionRate(Integer productionConversionRate) {
        this.productionConversionRate = productionConversionRate;
    }

    public Integer getMinProductionQty() {
        return minProductionQty;
    }
    public void setMinProductionQty(Integer minProductionQty) {
        this.minProductionQty = minProductionQty;
    }

    public BigDecimal getProductionLossRate() {
        return productionLossRate;
    }
    public void setProductionLossRate(BigDecimal productionLossRate) {
        this.productionLossRate = productionLossRate;
    }

    public String getSentUnit() {
        return sentUnit;
    }
    public void setSentUnit(String sentUnit) {
        this.sentUnit = sentUnit;
    }

    public Integer getSentConversionRate() {
        return sentConversionRate;
    }
    public void setSentConversionRate(Integer sentConversionRate) {
        this.sentConversionRate = sentConversionRate;
    }

    public Integer getMinSentQty() {
        return minSentQty;
    }
    public void setMinSentQty(Integer minSentQty) {
        this.minSentQty = minSentQty;
    }

    public Boolean getConsume() {
        return isConsume;
    }
    public void setConsume(Boolean isConsume) {
        this.isConsume = isConsume;
    }

    public Integer getValidityDays() {
        return validityDays;
    }
    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }

    public String getMainLineWarehouse() {
        return mainLineWarehouse;
    }
    public void setMainLineWarehouse(String mainLineWarehouse) {
        this.mainLineWarehouse = mainLineWarehouse;
    }

    public String getMainLineStorage() {
        return mainLineStorage;
    }
    public void setMainLineStorage(String mainLineStorage) {
        this.mainLineStorage = mainLineStorage;
    }

    public String getPositiveImageUrl() {
        return positiveImageUrl;
    }
    public void setPositiveImageUrl(String positiveImageUrl) {
        this.positiveImageUrl = positiveImageUrl;
    }

    public String getNegativeImage() {
        return negativeImage;
    }
    public void setNegativeImage(String negativeImage) {
        this.negativeImage = negativeImage;
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