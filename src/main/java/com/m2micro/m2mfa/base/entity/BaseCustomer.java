package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import com.m2micro.framework.starter.entity.validgroup.Add;
import com.m2micro.framework.starter.entity.validgroup.Profile;
import com.m2micro.framework.starter.entity.validgroup.Update;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

/**
 * 客户基本资料档
 * @author liaotao
 * @since 2018-11-26
 */
@Entity
@ApiModel(value="BaseCustomer对象", description="客户基本资料档")
public class BaseCustomer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String customerId;
    @ApiModelProperty(value = "编号")
    @Size(max=32,message = "编号字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String code;
    @ApiModelProperty(value = "简码")
    @Size(max=32,message = "简码字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String abbreviation;
    @ApiModelProperty(value = "简称")
    @Size(max=32,message = "简称字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    @ApiModelProperty(value = "全称")
    @Size(max=32,message = "全称字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String fullname;
    @ApiModelProperty(value = "类型")
    @Size(max=32,message = "类型字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String category;
    @Transient
    @ApiModelProperty(value = "类型名称")
    @Size(max=32,message = "类型名称字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String categoryName;
    @Size(max=32,message = "区域字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "区域")
    private String area;
    @Size(min=11, max=11,message = "电话号码格式有误",groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^$|^1[3|4|5|8][0-9]\\d{8}$", message = "手机号码格式不正确", groups = {Add.class, Update.class, Profile.class})
    @ApiModelProperty(value = "电话号码")
    private String telephone;
    @ApiModelProperty(value = "传真号码")
    @Size(max=32,message = "传真号码格式有误",groups = {AddGroup.class, UpdateGroup.class})
    private String fax;
    @ApiModelProperty(value = "公司主页")
    @Size(max=32,message = "公司主页字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String web;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    @Size(max=32,message = "描述字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String description;

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWeb() {
        return web;
    }
    public void setWeb(String web) {
        this.web = web;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}