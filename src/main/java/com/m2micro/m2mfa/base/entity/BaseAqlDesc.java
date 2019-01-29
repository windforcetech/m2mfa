package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 抽样标准(aql)-主档
 * @author liaotao
 * @since 2019-01-29
 */
@Entity
@ApiModel(value="BaseAqlDesc对象", description="抽样标准(aql)-主档")
public class BaseAqlDesc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String aqlId;
    private String aqlCode;
    private String aqlName;
    private String category;
    private Boolean enabled;
    private String description;

    public String getAqlId() {
        return aqlId;
    }
    public void setAqlId(String aqlId) {
        this.aqlId = aqlId;
    }

    public String getAqlCode() {
        return aqlCode;
    }
    public void setAqlCode(String aqlCode) {
        this.aqlCode = aqlCode;
    }

    public String getAqlName() {
        return aqlName;
    }
    public void setAqlName(String aqlName) {
        this.aqlName = aqlName;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
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