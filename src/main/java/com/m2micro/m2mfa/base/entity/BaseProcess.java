package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工序基本档
 * @author chenshuhong
 * @since 2018-12-14
 */
@Entity
@ApiModel(value="BaseProcess对象", description="工序基本档")
public class BaseProcess extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String processId;
    @NotEmpty(message="工艺编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "编号")
    private String processCode;
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称")
    private String processName;
    @NotEmpty(message="数据采集方式不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "数据采集方式")
    private String collection;
    @NotEmpty(message="类型",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "类型")
    private String category;
    @NotNull(message="有效否",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @NotEmpty(message="描述不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述")
    private String description;

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessCode() {
        return processCode;
    }
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCollection() {
        return collection;
    }
    public void setCollection(String collection) {
        this.collection = collection;
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