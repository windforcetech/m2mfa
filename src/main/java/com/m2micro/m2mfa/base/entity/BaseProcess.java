package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @Size(max=32,message = "工艺编号长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "编号")
    private String processCode;
    @NotEmpty(message="名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "名称长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "名称")
    private String processName;
    @NotEmpty(message="",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "数据采集方式长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "数据采集方式")
    private String collection;
    @NotEmpty(message="类型",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "类型长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "类型")
    private String category;
    @NotNull(message="有效否",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;

    @Size(max=32,message = "描述长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述")
    private String description;
    @Transient
    @ApiModelProperty("工序类型名称")
    private String  categoryName;
    @Transient
    @ApiModelProperty("采集方式名称")
    private String  collectionName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public BaseProcess() {
    }

    public BaseProcess(@NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class}) String processId, @NotEmpty(message = "工艺编号不能为空", groups = {AddGroup.class, UpdateGroup.class}) @Size(max = 32, message = "工艺编号长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String processCode, @NotEmpty(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class}) @Size(max = 32, message = "名称长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String processName, @NotEmpty(message = "数据采集方式不能为空", groups = {AddGroup.class, UpdateGroup.class}) @Size(max = 32, message = "数据采集方式长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String collection, @NotEmpty(message = "类型", groups = {AddGroup.class, UpdateGroup.class}) @Size(max = 32, message = "类型长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String category, @NotNull(message = "有效否", groups = {AddGroup.class, UpdateGroup.class}) Boolean enabled, @NotEmpty(message = "描述不能为空", groups = {AddGroup.class, UpdateGroup.class}) @Size(max = 32, message = "描述长度不能大于32位", groups = {AddGroup.class, UpdateGroup.class}) String description, String categoryName, String collectionName) {
        this.processId = processId;
        this.processCode = processCode;
        this.processName = processName;
        this.collection = collection;
        this.category = category;
        this.enabled = enabled;
        this.description = description;
        this.categoryName = categoryName;
        this.collectionName = collectionName;
    }
}