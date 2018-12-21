package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机台主档
 * @author liaotao
 * @since 2018-11-22
 */
@Entity
@ApiModel(value="BaseMachine对象", description="机台主档")
public class BaseMachine extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String machineId;
    @ApiModelProperty(value = "编号")
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @ApiModelProperty(value = "名称")
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "名称id")
    @NotEmpty(message="名称id不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String id;
    @ApiModelProperty(value = "固资编号")
    private String assdtId;
    @ApiModelProperty(value = "出厂序列号")
    private String serialNumber;
    @ApiModelProperty(value = "设备类型")
    private String categoryId;
    @ApiModelProperty(value = "摆放位置")
    private String placement;
    @ApiModelProperty(value = "摆放位置名称")
    private String placementName;
    @ApiModelProperty(value = "设备状态")
    private String flag;
    @Transient
    @ApiModelProperty(value = "设备状态名称")
    private String flagName;
    @ApiModelProperty(value = "归属部门")
    private String departmentId;
    @Transient
    @ApiModelProperty(value = "归属部门名称")
    private String departmentName;
    @ApiModelProperty(value = "购入日期")
    private Date purchaseDate;
    @ApiModelProperty(value = "购入费用")
    private Double purchaseCost;
    @ApiModelProperty(value = "使用寿命")
    private Double life;
    @ApiModelProperty(value = "设计稼动时间")
    private Double utilizationTime;
    @ApiModelProperty(value = "送料器容量")
    private Double tankCapacity;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "对应维修人员")
    private String maintenanceStaff;
    @ApiModelProperty(value = "对应技术人员")
    private String technicalStaff;
    @ApiModelProperty(value = "对应管理人员")
    private String managerStaff;
    @ApiModelProperty(value = "当前生产的工单")
    private String newMono;
    @ApiModelProperty(value = "参考图片")
    private String image;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
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

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getPurchaseCost() {
        return purchaseCost;
    }
    public void setPurchaseCost(Double purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public Double getLife() {
        return life;
    }
    public void setLife(Double life) {
        this.life = life;
    }

    public Double getUtilizationTime() {
        return utilizationTime;
    }
    public void setUtilizationTime(Double utilizationTime) {
        this.utilizationTime = utilizationTime;
    }

    public Double getTankCapacity() {
        return tankCapacity;
    }
    public void setTankCapacity(Double tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getNewMono() {
        return newMono;
    }
    public void setNewMono(String newMono) {
        this.newMono = newMono;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSortCode() {
        return sortCode;
    }
    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public Integer getEnabled() {
        return enabled;
    }
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}