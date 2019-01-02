package com.m2micro.m2mfa.record.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

/**
 * .参数记录表
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordParameter对象", description=".参数记录表")
public class MesRecordParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工索引")
    private String rwId;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "使用参数表")
    private String parameterTableId;
    @ApiModelProperty(value = "参数")
    private String parameterId;
    @ApiModelProperty(value = "输入值")
    private BigDecimal value;
    @ApiModelProperty(value = "是否异常")
    private Integer isAbnormal;
    @ApiModelProperty(value = "是否确认")
    private Integer isConfirm;
    @ApiModelProperty(value = "确认人")
    private String confirmBy;
    @ApiModelProperty(value = "确认时间")
    private Date confirmOn;
    @ApiModelProperty(value = "调机时间",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createOn;
    @ApiModelProperty(value = "调机人")
    private String createBy;

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getRwId() {
        return rwId;
    }
    public void setRwId(String rwId) {
        this.rwId = rwId;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getParameterTableId() {
        return parameterTableId;
    }
    public void setParameterTableId(String parameterTableId) {
        this.parameterTableId = parameterTableId;
    }

    public String getParameterId() {
        return parameterId;
    }
    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getIsAbnormal() {
        return isAbnormal;
    }
    public void setIsAbnormal(Integer isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }
    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getConfirmBy() {
        return confirmBy;
    }
    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Date getConfirmOn() {
        return confirmOn;
    }
    public void setConfirmOn(Date confirmOn) {
        this.confirmOn = confirmOn;
    }



}