package com.m2micro.m2mfa.barcode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("排产单")
public class ScheduleObj {
    @ApiModelProperty("排产单id")
    private String scheduleId;
    @ApiModelProperty("排产单号")
    private String scheduleNo;
    @ApiModelProperty("机台id")
    private String machineId;
    @ApiModelProperty("料件id")
    private String partId;
    @ApiModelProperty("料件编号")
    private String partNo;
    @ApiModelProperty("品名")
    private String partName;
    @ApiModelProperty("排产数量")
    private String scheduleQty;
    @ApiModelProperty("机台编号")
    private String machineName;

    @ApiModelProperty("规格")
    private String partSpec;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户编号")
    private String customerCode;

    @ApiModelProperty("料件关联模板信息")
    public List<TemplateObj> getTemplateObjList() {
        return templateObjList;
    }

    @ApiModelProperty("项次")
    private Integer orderSeq;

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getPartSpec() {
        return partSpec;
    }

    public void setPartSpec(String partSpec) {
        this.partSpec = partSpec;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public void setTemplateObjList(List<TemplateObj> templateObjList) {
        this.templateObjList = templateObjList;
    }


    private List<TemplateObj> templateObjList;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getScheduleQty() {
        return scheduleQty;
    }

    public void setScheduleQty(String scheduleQty) {
        this.scheduleQty = scheduleQty;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public ScheduleObj() {
    }
}
