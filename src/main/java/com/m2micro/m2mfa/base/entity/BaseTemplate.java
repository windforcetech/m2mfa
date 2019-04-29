package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 标签模板
 * @author liaotao
 * @since 2019-01-22
 */
@Entity
@ApiModel(value="BaseTemplate对象", description="标签模板")
public class BaseTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ApiModelProperty(value = "主键")
    private String id;
    @Size(max=50,message = "编号字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "编号")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @Size(max=50,message = "标签类型字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="标签类型不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签类型")
    private String category;
    @Transient
    @ApiModelProperty(value = "标签类型名称")
    private String categoryName;
    @ApiModelProperty(value = "版本")
    private Integer version;
    @ApiModelProperty(value = "标签图片")
    private String imageUrl;
    @Size(max=50,message = "标签模板字节不能大于200位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="标签模板不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签模板")
    private String labelFileUrl;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @Transient
    @ApiModelProperty(value = "模板变量")
    private List<BaseTemplateVar> baseTemplateVars;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLabelFileUrl() {
        return labelFileUrl;
    }
    public void setLabelFileUrl(String labelFileUrl) {
        this.labelFileUrl = labelFileUrl;
    }

    public Integer getSortCode() {
        return sortCode;
    }
    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
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

    public List<BaseTemplateVar> getBaseTemplateVars() {
        return baseTemplateVars;
    }

    public void setBaseTemplateVars(List<BaseTemplateVar> baseTemplateVars) {
        this.baseTemplateVars = baseTemplateVars;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
