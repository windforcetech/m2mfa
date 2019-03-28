package com.m2micro.m2mfa.barcode.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 标签打印表单
 * @author liaotao
 * @since 2019-03-27
 */
@Entity
@ApiModel(value="BarcodePrintApply对象", description="标签打印表单")
public class BarcodePrintApply extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String printCategory;
    private String category;
    private String source;
    private Integer sequence;
    private String lableCategory;
    private String templateId;
    private String customerNo;
    private String partId;
    private Integer qty;
    private Integer checkFlag;
    private Date checkOn;
    private String checkBy;
    private Date collarOn;
    private String collarBy;
    private Integer flag;
    private Integer sortCode;
    private Boolean enabled;
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPrintCategory() {
        return printCategory;
    }
    public void setPrintCategory(String printCategory) {
        this.printCategory = printCategory;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getLableCategory() {
        return lableCategory;
    }
    public void setLableCategory(String lableCategory) {
        this.lableCategory = lableCategory;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public Integer getQty() {
        return qty;
    }
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getCheckFlag() {
        return checkFlag;
    }
    public void setCheckFlag(Integer checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Date getCheckOn() {
        return checkOn;
    }
    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getCheckBy() {
        return checkBy;
    }
    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public Date getCollarOn() {
        return collarOn;
    }
    public void setCollarOn(Date collarOn) {
        this.collarOn = collarOn;
    }

    public String getCollarBy() {
        return collarBy;
    }
    public void setCollarBy(String collarBy) {
        this.collarBy = collarBy;
    }

    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getSortCode() {
        return sortCode;
    }
    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
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