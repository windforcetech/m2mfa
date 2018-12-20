package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
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
 * 模具主档
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value="BaseMold对象", description="模具主档")
public class BaseMold extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String moldId;
    @ApiModelProperty(value = "编号")
    @Size(max=32,message = "长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @Size(max=32,message = "长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称")
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @Size(max=32,message = "长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "固资编号")
    private String assdtId;
    @ApiModelProperty(value = "所属客户")
    private String customerId;
    @Transient
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "分类")
    @NotEmpty(message="分类不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String categoryId;
    @Transient
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "类型")
    @NotEmpty(message="类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String typeId;
    @ApiModelProperty(value = "摆放位置")
    private String placement;
    @Transient
    @ApiModelProperty(value = "摆放位置")
    private String placementName;
    @ApiModelProperty(value = "状态")
    private String flag;
    @Transient
    @ApiModelProperty(value = "状态名称")
    private String flagName;
    @ApiModelProperty(value = "归属部门")
    private String departmentId;
    @ApiModelProperty(value = "开模日期")
    private Date moldDate;
    @ApiModelProperty(value = "开模费用")
    private BigDecimal moldCost;
    @ApiModelProperty(value = "使用寿命")
    private BigDecimal life;
    @ApiModelProperty(value = "设计产出")
    private BigDecimal output;
    @ApiModelProperty(value = "对应维修人员")
    private String maintenanceStaff;
    @ApiModelProperty(value = "对应技术人员")
    private String technicalStaff;
    @ApiModelProperty(value = "对应管理人员")
    private String managerStaff;
    @ApiModelProperty(value = "参考图片")
    private String imageUrl;
    @ApiModelProperty(value = "脱模方式")
    private Integer stripp;
    @ApiModelProperty(value = "是否使用模芯")
    private Boolean usemandrel;
    @ApiModelProperty(value = "脱模速度")
    private BigDecimal speed;
    @ApiModelProperty(value = "设计产能")
    private BigDecimal designCapacity;
    @ApiModelProperty(value = "标准产能")
    private BigDecimal standardCapacity;
    @ApiModelProperty(value = "实际产能")
    private BigDecimal actualCapacity;
    @ApiModelProperty(value = "模穴数量")
    private Integer cavityQty;
    @ApiModelProperty(value = "可用模穴数量")
    private Integer cavityAvailable;
    @ApiModelProperty(value = "可用模穴说明")
    private String cavityRemark;
    @ApiModelProperty(value = "开模厂商")
    private String supplierId;
    @ApiModelProperty(value = "模具结构")
    private String moldStructure;
    @ApiModelProperty(value = "工程数")
    private Integer projectQty;
    @ApiModelProperty(value = "开模高度")
    private BigDecimal openHeight;
    @ApiModelProperty(value = "闭模高度")
    private BigDecimal closeHeight;
    @ApiModelProperty(value = "模具规格长")
    private BigDecimal l;
    @ApiModelProperty(value = "模具规格宽")
    private BigDecimal w;
    @ApiModelProperty(value = "模具规格高")
    private BigDecimal h;
    @ApiModelProperty(value = "材质")
    private String material;
    @ApiModelProperty(value = "设计图档")
    private String designFileUrl;
    @ApiModelProperty(value = "其他参考文档")
    private String otherFileUrl;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAssdtId() {
        return assdtId;
    }
    public void setAssdtId(String assdtId) {
        this.assdtId = assdtId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPlacement() {
        return placement;
    }
    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public Date getMoldDate() {
        return moldDate;
    }
    public void setMoldDate(Date moldDate) {
        this.moldDate = moldDate;
    }

    public BigDecimal getMoldCost() {
        return moldCost;
    }
    public void setMoldCost(BigDecimal moldCost) {
        this.moldCost = moldCost;
    }

    public BigDecimal getLife() {
        return life;
    }
    public void setLife(BigDecimal life) {
        this.life = life;
    }

    public BigDecimal getOutput() {
        return output;
    }
    public void setOutput(BigDecimal output) {
        this.output = output;
    }

    public String getMaintenanceStaff() {
        return maintenanceStaff;
    }
    public void setMaintenanceStaff(String maintenanceStaff) {
        this.maintenanceStaff = maintenanceStaff;
    }

    public String getTechnicalStaff() {
        return technicalStaff;
    }
    public void setTechnicalStaff(String technicalStaff) {
        this.technicalStaff = technicalStaff;
    }

    public String getManagerStaff() {
        return managerStaff;
    }
    public void setManagerStaff(String managerStaff) {
        this.managerStaff = managerStaff;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStripp() {
        return stripp;
    }
    public void setStripp(Integer stripp) {
        this.stripp = stripp;
    }

    public Boolean getUsemandrel() {
        return usemandrel;
    }
    public void setUsemandrel(Boolean usemandrel) {
        this.usemandrel = usemandrel;
    }

    public BigDecimal getSpeed() {
        return speed;
    }
    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getDesignCapacity() {
        return designCapacity;
    }
    public void setDesignCapacity(BigDecimal designCapacity) {
        this.designCapacity = designCapacity;
    }

    public BigDecimal getStandardCapacity() {
        return standardCapacity;
    }
    public void setStandardCapacity(BigDecimal standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public BigDecimal getActualCapacity() {
        return actualCapacity;
    }
    public void setActualCapacity(BigDecimal actualCapacity) {
        this.actualCapacity = actualCapacity;
    }

    public Integer getCavityQty() {
        return cavityQty;
    }
    public void setCavityQty(Integer cavityQty) {
        this.cavityQty = cavityQty;
    }

    public Integer getCavityAvailable() {
        return cavityAvailable;
    }
    public void setCavityAvailable(Integer cavityAvailable) {
        this.cavityAvailable = cavityAvailable;
    }

    public String getCavityRemark() {
        return cavityRemark;
    }
    public void setCavityRemark(String cavityRemark) {
        this.cavityRemark = cavityRemark;
    }

    public String getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getMoldStructure() {
        return moldStructure;
    }
    public void setMoldStructure(String moldStructure) {
        this.moldStructure = moldStructure;
    }

    public Integer getProjectQty() {
        return projectQty;
    }
    public void setProjectQty(Integer projectQty) {
        this.projectQty = projectQty;
    }

    public BigDecimal getOpenHeight() {
        return openHeight;
    }
    public void setOpenHeight(BigDecimal openHeight) {
        this.openHeight = openHeight;
    }

    public BigDecimal getCloseHeight() {
        return closeHeight;
    }
    public void setCloseHeight(BigDecimal closeHeight) {
        this.closeHeight = closeHeight;
    }

    public BigDecimal getL() {
        return l;
    }
    public void setL(BigDecimal l) {
        this.l = l;
    }

    public BigDecimal getW() {
        return w;
    }
    public void setW(BigDecimal w) {
        this.w = w;
    }

    public BigDecimal getH() {
        return h;
    }
    public void setH(BigDecimal h) {
        this.h = h;
    }

    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDesignFileUrl() {
        return designFileUrl;
    }
    public void setDesignFileUrl(String designFileUrl) {
        this.designFileUrl = designFileUrl;
    }

    public String getOtherFileUrl() {
        return otherFileUrl;
    }
    public void setOtherFileUrl(String otherFileUrl) {
        this.otherFileUrl = otherFileUrl;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPlacementName() {
        return placementName;
    }

    public void setPlacementName(String placementName) {
        this.placementName = placementName;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }
}