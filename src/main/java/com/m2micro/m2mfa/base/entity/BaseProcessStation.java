package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工序工位关系
 * @author chenshuhong
 * @since 2018-12-14
 */
@Entity
@ApiModel(value="BaseProcessStation对象", description="工序工位关系")
public class BaseProcessStation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String psId;
    @ApiModelProperty(value = "工序主键")
    private String processId;
    @ApiModelProperty(value = "行为主键")
    private String stationId;
    @ApiModelProperty(value = "步骤")
    private Integer step;
    @ApiModelProperty(value = "允许跳过")
    private Integer jump;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getPsId() {
        return psId;
    }
    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Integer getStep() {
        return step;
    }
    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getJump() {
        return jump;
    }
    public void setJump(Integer jump) {
        this.jump = jump;
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