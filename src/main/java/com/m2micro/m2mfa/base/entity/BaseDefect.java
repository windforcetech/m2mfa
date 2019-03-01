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
 * 不良現象代碼
 * @author chenshuhong
 * @since 2019-01-24
 */
@Entity
@ApiModel(value="BaseDefect对象", description="不良現象代碼")
public class BaseDefect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private String ectId;
    @ApiModelProperty(value = "不良代码")
    @Id
    private String ectCode;
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "名称长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "不良名称")
    private String ectName;
    @ApiModelProperty(value = "不良类型")
    private String category;
    @ApiModelProperty(value = "不良分类")
    private Integer sortCode;
    @ApiModelProperty(value = "不良状态")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

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



}
