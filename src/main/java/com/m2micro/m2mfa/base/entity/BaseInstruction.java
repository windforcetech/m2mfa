package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 作业指导书
 * @author chengshuhong
 * @since 2019-03-04
 */

@Entity
@ApiModel(value="BaseInstruction对象", description="作业指导书")
public class BaseInstruction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String instructionId;
    @ApiModelProperty(value = "编号")
    private String instructionCode;
    @ApiModelProperty(value = "名称")
    private String instructionName;
    @ApiModelProperty(value = "类型")
    private String category;
    @Transient
    @ApiModelProperty(value = "类型名称")
    private String categoryName;
    @ApiModelProperty(value = "版本")
    private Integer revsion;
    @ApiModelProperty(value = "指导书文件")
    private String fileUrl;
    @ApiModelProperty(value = "文件后缀名")
    private String extension;
    @ApiModelProperty(value = "审核状态")
    private Boolean checkFlag;
    @ApiModelProperty(value = "审核日期")
    private Date checkOn;
    @ApiModelProperty(value = "审核用户主键")
    private String checkBy;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    public String getInstructionName() {
        return instructionName;
    }

    public void setInstructionName(String instructionName) {
        this.instructionName = instructionName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getRevsion() {
        return revsion;
    }

    public void setRevsion(Integer revsion) {
        this.revsion = revsion;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Boolean getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Boolean checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Date getCheckOn() {
        return checkOn;
    }

    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
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
