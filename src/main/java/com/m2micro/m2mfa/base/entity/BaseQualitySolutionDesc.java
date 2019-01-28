package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 检验方案主档
 * @author liaotao
 * @since 2019-01-28
 */
@Entity
@ApiModel(value="BaseQualitySolutionDesc对象", description="检验方案主档")
public class BaseQualitySolutionDesc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String solutionId;
    @ApiModelProperty(value = "编号")
    private String solutionCode;
    @ApiModelProperty(value = "名称")
    private String solutionName;
    @ApiModelProperty(value = "抽检方案")
    private String aqlId;
    @ApiModelProperty(value = "作业指导书")
    private String instructionId;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getSolutionId() {
        return solutionId;
    }
    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getSolutionCode() {
        return solutionCode;
    }
    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public String getSolutionName() {
        return solutionName;
    }
    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getAqlId() {
        return aqlId;
    }
    public void setAqlId(String aqlId) {
        this.aqlId = aqlId;
    }

    public String getInstructionId() {
        return instructionId;
    }
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
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