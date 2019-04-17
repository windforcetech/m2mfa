package com.m2micro.m2mfa.base.entity;


import javax.jdo.annotations.Transactional;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 不良現象代碼
 * @author liaotao
 * @since 2019-03-05
 */
@Entity
@ApiModel(value="BaseDefect对象", description="不良現象代碼")
public class BaseDefect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    private String ectId;
    private String ectCode;
    private String ectName;
    private String category;
    private Integer sortCode;
    private Boolean enabled;
    private String description;
    @Transactional
    private String categoryName;

    public String getEctId() {
        return ectId;
    }

    public void setEctId(String ectId) {
        this.ectId = ectId;
    }

    public String getEctCode() {
        return ectCode;
    }

    public void setEctCode(String ectCode) {
        this.ectCode = ectCode;
    }

    public String getEctName() {
        return ectName;
    }

    public void setEctName(String ectName) {
        this.ectName = ectName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
