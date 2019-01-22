package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模板变量
 * @author liaotao
 * @since 2019-01-22
 */
@Entity
@ApiModel(value="BaseTemplateVar对象", description="模板变量")
public class BaseTemplateVar extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @ApiModelProperty(value = "模板主键")
    private String templateId;
    @ApiModelProperty(value = "参数编号")
    private String number;
    @ApiModelProperty(value = "参数名称")
    private String name;
    @ApiModelProperty(value = "规则主键")
    private String ruleId;
    @ApiModelProperty(value = "默认值")
    private String defaults;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getRuleId() {
        return ruleId;
    }
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
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



}