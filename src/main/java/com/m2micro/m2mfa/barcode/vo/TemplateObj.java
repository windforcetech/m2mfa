package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("模板信息")
public class TemplateObj {
    @ApiModelProperty("模板id")
    private String id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("模板描述")
    private String description;
    @ApiModelProperty("模板版本")
    private String version;
    @ApiModelProperty("标签类型id")
    private String tagId;
    @ApiModelProperty("标签类型名称")
    private String tagName;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public TemplateObj() {
    }
}
