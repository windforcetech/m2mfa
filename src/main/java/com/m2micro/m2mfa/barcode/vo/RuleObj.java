package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("规则")
public class RuleObj {
    @ApiModelProperty("规则id")
    private String id;

    @ApiModelProperty("位置")
    private Integer position;

    @ApiModelProperty("种类id")
    private String category;

    @ApiModelProperty("默认值")
    private String defaults;

    @ApiModelProperty("长度")
    private  Integer length;

    @ApiModelProperty("进制")
    private Integer ary;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getAry() {
        return ary;
    }

    public void setAry(Integer ary) {
        this.ary = ary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }
}
