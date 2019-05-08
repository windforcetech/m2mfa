package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel("打印申请")
public class PrintApplyObj {
    @ApiModelProperty("申请单号")
    private String applyId;
    @ApiModelProperty("申请类型")
    private String printCategory;
    @ApiModelProperty("来源类型Id")
    private String category;
    @ApiModelProperty("来源类型")
    private String sourceType;

    @ApiModelProperty("来源类型Id")
    private String sourceId;
    @ApiModelProperty("来源单号id")
    private String source;

    @ApiModelProperty("来源单号")
    private String scheduleNo;
    @ApiModelProperty("标签类型")
    private String flagType;
    @ApiModelProperty("标签模板")
    private String templateName;
    @ApiModelProperty("模板id")
    private String templateId;
    @ApiModelProperty("版本")
    private Integer templateVersion;
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户编号")
    private String customerCode;
    @ApiModelProperty("料件编号")
    private String partNo;
    @ApiModelProperty("数量")
    private Integer qty;
    @ApiModelProperty("申请时间")
    private Date checkOn;
    @ApiModelProperty("状态")
    private String flag;
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("审核")
    private Integer checkFlag;

    @ApiModelProperty("有效")
    private Boolean enabled;

    @ApiModelProperty("规格")
    private String spec;
//    @ApiModelProperty("项次")
//    private Integer sequence;
    @ApiModelProperty("料件id")
    private String partId;
    @ApiModelProperty("品名")
    private String partName;
    @ApiModelProperty("模板数据")

    private TemplatePrintObj templatePrintObj;


    @ApiModelProperty("包装数据")
    private PackObj packObj;

    @ApiModelProperty("工单号码")
    private String moNumber;

    @ApiModelProperty("工单项次")
    private String orderSeq;


    @ApiModelProperty("来源类型名称")
    private String categoryName;



    @ApiModelProperty("打印条码列表")
    private List<PrintResourceObj> printResourceObjList;

    public List<PrintResourceObj> getPrintResourceObjList() {
        return printResourceObjList;
    }

    public void setPrintResourceObjList(List<PrintResourceObj> printResourceObjList) {
        this.printResourceObjList = printResourceObjList;
    }

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public PackObj getPackObj() {
        return packObj;
    }

    public void setPackObj(PackObj packObj) {
        this.packObj = packObj;
    }

    public TemplatePrintObj getTemplatePrintObj() {
        return templatePrintObj;
    }

    public void setTemplatePrintObj(TemplatePrintObj templatePrintObj) {
        this.templatePrintObj = templatePrintObj;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getFlagType() {
        return flagType;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }



    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Date getCheckOn() {
        return checkOn;
    }

    public void setCheckOn(Date checkOn) {
        this.checkOn = checkOn;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlg() {
        return flag;
    }

    public void setFlg(String flg) {
        this.flag = flg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(Integer checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(Integer templateVersion) {
        this.templateVersion = templateVersion;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
