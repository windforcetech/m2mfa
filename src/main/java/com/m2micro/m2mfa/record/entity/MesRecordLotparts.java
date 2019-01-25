package com.m2micro.m2mfa.record.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 加料记录表
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordLotparts对象", description="加料记录表")
public class MesRecordLotparts implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工主键")
    private String rwId;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "料号id")
    private String partId;
    @ApiModelProperty(value = "批号(箱号)")
    private String lotNo;
    @ApiModelProperty(value = "投入数量")
    private Integer inputSize;
    @ApiModelProperty(value = "投入时间",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createOn;
    @ApiModelProperty(value = "投入职员")
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

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getLotNo() {
        return lotNo;
    }
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Integer getInputSize() {
        return inputSize;
    }
    public void setInputSize(Integer inputSize) {
        this.inputSize = inputSize;
    }



}