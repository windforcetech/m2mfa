package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作业指导书关联
 * @author chengshuhong
 * @since 2019-03-04
 */
@Entity
@ApiModel(value="BasePartInstruction对象", description="作业指导书关联")
public class BasePartInstruction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "工位主键")
    private String stationId;
    @ApiModelProperty(value = "指导书主键")
    private String instructionId;
    @ApiModelProperty(value = "生效日期")
    private Date effectiveDate;
    @ApiModelProperty(value = "失效日期")
    private Date invalidDate;
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

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getInstructionId() {
        return instructionId;
    }
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getInvalidDate() {
        return invalidDate;
    }
    public void setInvalidDate(Date invalidDate) {
        this.invalidDate = invalidDate;
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