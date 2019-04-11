package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("模板变量信息")
public class TemplateVarObj {
    @ApiModelProperty("变量id")
    private String id;
    @ApiModelProperty("变量名")
    private String name;
    @ApiModelProperty("规则Id")
    private String ruleId;

    @ApiModelProperty("规则列表")
    private List<RuleObj> ruleObjList;

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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public List<RuleObj> getRuleObjList() {
        return ruleObjList;
    }

    public void setRuleObjList(List<RuleObj> ruleObjList) {
        this.ruleObjList = ruleObjList;
    }
}
