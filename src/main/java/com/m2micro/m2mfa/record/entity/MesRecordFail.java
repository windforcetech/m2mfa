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
 * 不良输入记录
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordFail对象", description="不良输入记录")
public class MesRecordFail implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工索引")
    private String rwId;
    @ApiModelProperty(value = "序号")
    private String serialNumber;
    @ApiModelProperty(value = "母批")
    private String parentSerialNumber;
    @ApiModelProperty(value = "不良现象代码")
    private String defectCode;
    @ApiModelProperty(value = "数量")
    private long qty;
    @ApiModelProperty(value = "维修状态")
    private Integer repairFlag;
    @ApiModelProperty(value = "回流数量")
    private Integer backflowQty;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapQty;
    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date closeOn;
    @ApiModelProperty(value = "提交时间",example = "2018-11-21 12:00:00")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createOn;

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
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

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getParentSerialNumber() {
        return parentSerialNumber;
    }
    public void setParentSerialNumber(String parentSerialNumber) {
        this.parentSerialNumber = parentSerialNumber;
    }

    public String getDefectCode() {
        return defectCode;
    }
    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public Long getQty() {
        return qty;
    }
    public void setQty(long qty) {
        this.qty = qty;
    }

    public Integer getRepairFlag() {
        return repairFlag;
    }
    public void setRepairFlag(Integer repairFlag) {
        this.repairFlag = repairFlag;
    }

    public Integer getBackflowQty() {
        return backflowQty;
    }
    public void setBackflowQty(Integer backflowQty) {
        this.backflowQty = backflowQty;
    }

    public Integer getScrapQty() {
        return scrapQty;
    }
    public void setScrapQty(Integer scrapQty) {
        this.scrapQty = scrapQty;
    }

    public Date getCloseOn() {
        return closeOn;
    }
    public void setCloseOn(Date closeOn) {
        this.closeOn = closeOn;
    }



}
