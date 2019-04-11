package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("模板信息")
public class TemplatePrintObj {
    @ApiModelProperty("模板id")
    private String id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("模板描述")
    private String description;
    @ApiModelProperty("模板版本")
    private Integer version;
    @ApiModelProperty("标签类型Id")
    private String category;
    @ApiModelProperty("模板文件名")
    private String fileName;

    @ApiModelProperty("模板变量")
    private List<TemplateVarObj> templateVarObjList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<TemplateVarObj> getTemplateVarObjList() {
        return templateVarObjList;
    }

    public void setTemplateVarObjList(List<TemplateVarObj> templateVarObjList) {
        this.templateVarObjList = templateVarObjList;
    }
}
