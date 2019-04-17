package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 料件模板关联表
 *
 * @author liaotao
 * @since 2019-03-06
 */
@Entity
@ApiModel(value = "BasePartTemplate对象", description = "料件模板关联表")
public class BasePartTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @NotEmpty(message="id不能为空",groups = {UpdateGroup.class})
    private String id;
    @NotEmpty(message = "料件id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "料件id")
    private String partId;
    @NotEmpty(message = "模板id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "模板id")
    private String templateId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "有效否")
    private Boolean valid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }


}