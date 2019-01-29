package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 抽样标准(aql)-明细
 * @author liaotao
 * @since 2019-01-29
 */
@Entity
@ApiModel(value="BaseAqlDef对象", description="抽样标准(aql)-明细")
public class BaseAqlDef extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String aqlId;
    private Integer seq;
    private Integer batchMin;
    private Integer batchMax;
    private Integer extraction;
    private Integer rejected;
    private Boolean enabled;
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getAqlId() {
        return aqlId;
    }
    public void setAqlId(String aqlId) {
        this.aqlId = aqlId;
    }

    public Integer getSeq() {
        return seq;
    }
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getBatchMin() {
        return batchMin;
    }
    public void setBatchMin(Integer batchMin) {
        this.batchMin = batchMin;
    }

    public Integer getBatchMax() {
        return batchMax;
    }
    public void setBatchMax(Integer batchMax) {
        this.batchMax = batchMax;
    }

    public Integer getExtraction() {
        return extraction;
    }
    public void setExtraction(Integer extraction) {
        this.extraction = extraction;
    }

    public Integer getRejected() {
        return rejected;
    }
    public void setRejected(Integer rejected) {
        this.rejected = rejected;
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