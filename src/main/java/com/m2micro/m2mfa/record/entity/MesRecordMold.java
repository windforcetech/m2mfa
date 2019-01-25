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
 * 上模记录表
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordMold对象", description="上模记录表")
public class MesRecordMold implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工索引")
    private String rwId;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "使用模具")
    private String moldId;
    @ApiModelProperty(value = "下模否")
    private Integer underMold;
    @ApiModelProperty(value = "下模时间")
    private Date underOn;
    @ApiModelProperty(value = "下模人员")
    private String underBy;
    @ApiModelProperty(value = "上模时间",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createOn;
    @ApiModelProperty(value = "上模人员")
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

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public Integer getUnderMold() {
        return underMold;
    }
    public void setUnderMold(Integer underMold) {
        this.underMold = underMold;
    }

    public Date getUnderOn() {
        return underOn;
    }
    public void setUnderOn(Date underOn) {
        this.underOn = underOn;
    }

    public String getUnderBy() {
        return underBy;
    }
    public void setUnderBy(String underBy) {
        this.underBy = underBy;
    }



}