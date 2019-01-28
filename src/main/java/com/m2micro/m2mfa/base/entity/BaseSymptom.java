package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 不良原因代码
 * @author liaotao
 * @since 2019-01-28
 */
@Entity
@ApiModel(value="BaseSymptom对象", description="不良原因代码")
public class BaseSymptom extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String symptomId;
    @ApiModelProperty(value = "编号")
    private String symptomCode;
    @ApiModelProperty(value = "名称")
    private String symptomName;
    @ApiModelProperty(value = "类型")
    private String category;
    @Transient
    @ApiModelProperty(value = "类型名称")
    private String categoryName;
    @ApiModelProperty(value = "排序码")
    private Integer sortCode;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSymptomId() {
        return symptomId;
    }
    public void setSymptomId(String symptomId) {
        this.symptomId = symptomId;
    }

    public String getSymptomCode() {
        return symptomCode;
    }
    public void setSymptomCode(String symptomCode) {
        this.symptomCode = symptomCode;
    }

    public String getSymptomName() {
        return symptomName;
    }
    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSortCode() {
        return sortCode;
    }
    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public Integer getEnabled() {
        return enabled;
    }
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}