package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 检验方案明细
 * @author liaotao
 * @since 2019-01-28
 */
@Entity
@ApiModel(value="BaseQualitySolutionDef对象", description="检验方案明细")
public class BaseQualitySolutionDef extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "方案单头主键")
    private String solutionId;
    @ApiModelProperty(value = "项次")
    private Integer sequence;
    @ApiModelProperty(value = "检验项目主键")
    private String qitemId;
    @ApiModelProperty(value = "上限值")
    private BigDecimal upperLimit;
    @ApiModelProperty(value = "下限值")
    private BigDecimal lowerLimit;
    @ApiModelProperty(value = "中心值")
    private BigDecimal centralLimit;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSolutionId() {
        return solutionId;
    }
    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getQitemId() {
        return qitemId;
    }
    public void setQitemId(String qitemId) {
        this.qitemId = qitemId;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }
    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }
    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public BigDecimal getCentralLimit() {
        return centralLimit;
    }
    public void setCentralLimit(BigDecimal centralLimit) {
        this.centralLimit = centralLimit;
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