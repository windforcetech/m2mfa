package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 料件品质方案关联
 * @author liaotao
 * @since 2019-03-05
 */
@Entity
@ApiModel(value="BasePartQualitySolution对象", description="料件品质方案关联")
public class BasePartQualitySolution extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "料件编号")
    @Id
    private String partId;
    @ApiModelProperty(value = "检验方案id")
    private String solutionId;
    @ApiModelProperty(value = "巡检频率(h/次)")
    private Integer inspectionFrequency;
    @ApiModelProperty(value = "作业指导(sip)")
    private String instructionId;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getSolutionId() {
        return solutionId;
    }
    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public Integer getInspectionFrequency() {
        return inspectionFrequency;
    }
    public void setInspectionFrequency(Integer inspectionFrequency) {
        this.inspectionFrequency = inspectionFrequency;
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