package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
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
    @ApiModelProperty(value = "主键")
    @Id
    private String aqlId;
    @ApiModelProperty(value = "编号")
    private String aqlCode;
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "名称长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称")
    private String aqlName;
    @ApiModelProperty(value = "抽样方式")
    private String category;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
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
