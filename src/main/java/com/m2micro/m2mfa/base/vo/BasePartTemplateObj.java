package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "BasePartTemplateObj", description = "料件模板关联obj")
public class BasePartTemplateObj {

    private String id;
    @ApiModelProperty(value = "料件id")
    private String partId;
    @ApiModelProperty(value = "模板id")
    private String templateId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "有效否")
    private Boolean valid;
    @ApiModelProperty(value = "模板版本")
    private  String templateVersion;
    @ApiModelProperty(value = "料件编号")
    private  String partNo;
    @ApiModelProperty(value = "品名")
    private  String partName;
    @ApiModelProperty(value = "标签类型")
    private  String category;
    @ApiModelProperty(value = "模板名称")
    private String templateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
