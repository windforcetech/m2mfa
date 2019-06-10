package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseBarcodeRule;
import com.m2micro.m2mfa.base.entity.BaseBarcodeRuleDef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "BaseBarcodeRuleObj", description = "条形码规则定义")
public class BaseBarcodeRuleObj {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "条码编号")
    private String ruleCode;
    @ApiModelProperty(value = "名称")
    private String ruleName;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "条形码规则变量列表")
    private List<BaseBarcodeRuleDefineObj> baseBarcodeRuleDefineObjList;
    @ApiModelProperty(value = "批量删除变量")
    List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public BaseBarcodeRuleObj() {
        baseBarcodeRuleDefineObjList = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public List<BaseBarcodeRuleDefineObj> getBaseBarcodeRuleDefineObjList() {
        return baseBarcodeRuleDefineObjList;
    }

    public void setBaseBarcodeRuleDefineObjList(List<BaseBarcodeRuleDefineObj> baseBarcodeRuleDefineObjList) {
        this.baseBarcodeRuleDefineObjList = baseBarcodeRuleDefineObjList;
    }

    public BaseBarcodeRule copyBaseBarcodeRule() {

        BaseBarcodeRule bar = new BaseBarcodeRule();
        bar.setId(this.id);
        bar.setDescription(this.description);
        bar.setEnabled(this.enabled);
        bar.setRuleCode(this.ruleCode);
        bar.setRuleName(this.ruleName);
        bar.setSortCode(this.sortCode);
        return bar;
    }

    public List<BaseBarcodeRuleDef> copyBaseBarcodeRuleDef() {
        List<BaseBarcodeRuleDef> rs = new ArrayList<>();
        for (BaseBarcodeRuleDefineObj one : this.getBaseBarcodeRuleDefineObjList()) {
            BaseBarcodeRuleDef def = new BaseBarcodeRuleDef();
            def.setId(one.getId());
            def.setAry(one.getAry());
            def.setBarcodeId(one.getBarcodeId());
            def.setCategory(one.getCategory());
            def.setContent(one.getContent());
            def.setDefaults(one.getDefaults());
            def.setDescription(one.getDescription());
            def.setEnabled(one.getEnabled());
            def.setGroupBy(one.getGroupBy());
            def.setLength(one.getLength());
            def.setMaxNo(one.getMaxNo());
            def.setName(one.getName());
            def.setPosition(one.getPosition());
            def.setSortCode(one.getSortCode());
            rs.add(def);


        }
        return rs;
    }

    public  static BaseBarcodeRuleObj createSelf(BaseBarcodeRule rule,List<BaseBarcodeRuleDef> defs){
        BaseBarcodeRuleObj obj=new BaseBarcodeRuleObj();
        obj.setId(rule.getId());
        obj.setDescription(rule.getDescription());
        obj.setEnabled(rule.getEnabled());
        obj.setRuleCode(rule.getRuleCode());
        obj.setRuleName(rule.getRuleName());
        obj.setSortCode(rule.getSortCode());
        for (BaseBarcodeRuleDef one:defs){
            BaseBarcodeRuleDefineObj a=new BaseBarcodeRuleDefineObj();
            a.setAry(one.getAry());
            a.setBarcodeId(one.getBarcodeId());
            a.setCategory(one.getCategory());
            a.setContent(one.getContent());
            a.setDefaults(one.getDefaults());
            a.setDescription(one.getDescription());
            a.setEnabled(one.getEnabled());
            a.setGroupBy(one.getGroupBy());
            a.setId(one.getId());
            a.setLength(one.getLength());
            a.setMaxNo(one.getMaxNo());
            a.setName(one.getName());
            a.setPosition(one.getPosition());
            a.setSortCode(one.getSortCode());
            obj.getBaseBarcodeRuleDefineObjList().add(a);
        }
        return obj;

    }
}
