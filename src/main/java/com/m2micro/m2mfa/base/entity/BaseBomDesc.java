package com.m2micro.m2mfa.base.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.base.vo.BomDefObj;
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
@ApiModel(value = "BaseBomDesc对象", description = "料件物料清单")
public class BaseBomDesc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class})
    private String bomId;
    @ApiModelProperty(value = "料件编号")
    @Size(max = 50, message = "料件编号不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message = "料件编号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String partId;
    @ApiModelProperty(value = "版本号")
    private Integer version;
    @ApiModelProperty(value = "特性码")
    @Size(max = 50, message = "特性码不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    private String distinguish;
    @Size(max = 50, message = "类型不能大于50位", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "生效日期", example = "2019-1-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    @ApiModelProperty(value = "失效日期", example = "2019-5-21")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invalidDate;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;

    @ApiModelProperty(value = "描述")
    @Size(max = 200, message = "描述不能大于200位", groups = {AddGroup.class, UpdateGroup.class})
    private String description;
    @ApiModelProperty(value = "审核状态")
    private Boolean checkFlag;

    @ApiModelProperty(value = "审核日期", example = "2018-11-21 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @LastModifiedDate
    private Date checkOn;

    @ApiModelProperty(value = "审核用户主键")
    private String checkBy;

    @ApiModelProperty(value = "明细")
    @Transient
    private List<BaseBomDef> bomDefObjList;


    @ApiModelProperty(value = "料件物料清单取替代关联明细")
    @Transient
    private List<BaseBomSubstitute> bomSubstituteList;

    public List<BaseBomSubstitute> getBomSubstituteList() {
        return bomSubstituteList;
    }

    public void setBomSubstituteList(List<BaseBomSubstitute> bomSubstituteList) {
        this.bomSubstituteList = bomSubstituteList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<BaseBomDef> getBomDefObjList() {
        return bomDefObjList;
    }

    public void setBomDefObjList(List<BaseBomDef> bomDefObjList) {
        this.bomDefObjList = bomDefObjList;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDistinguish() {
        return distinguish;
    }

    public void setDistinguish(String distinguish) {
        this.distinguish = distinguish;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Boolean getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Boolean checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Date getCheckOn() {
        return checkOn;
    }

    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }
}