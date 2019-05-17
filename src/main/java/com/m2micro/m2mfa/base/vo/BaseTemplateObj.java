package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.m2mfa.base.entity.BaseTemplate;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "BaseTemplateObj", description = "模板定义")
public class BaseTemplateObj {


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
    @ApiModelProperty(value = "版本")
    private Integer version;
    @ApiModelProperty(value = "标签图片")
    private String imageUrl;
    @Size(max=50,message = "标签模板字节不能大于50位",groups = {AddGroup.class, UpdateGroup.class})
    @NotEmpty(message="标签模板不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "标签模板")
    private String labelFileUrl;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "模板变量")
    private List<BaseTemplateVarObj> templateVarObjList;

    @ApiModelProperty(value = " 模板")
    private  BaseTemplate baseTemplates;

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

    public List<BaseTemplateVarObj> getTemplateVarObjList() {
        return templateVarObjList;
    }

    public void setTemplateVarObjList(List<BaseTemplateVarObj> templateVarObjList) {
        this.templateVarObjList = templateVarObjList;
    }

    public BaseTemplate getBaseTemplates() {
        return baseTemplates;
    }

    public void setBaseTemplates(BaseTemplate baseTemplates) {
        this.baseTemplates = baseTemplates;
    }
}
