package com.m2micro.m2mfa.record.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 在制记录表
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesRecordWipRec对象", description="在制记录表")
public class MesRecordWipRec implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "上工表索引")
    private String rwId;
    @ApiModelProperty(value = "序号")
    private String serialNumber;
    @ApiModelProperty(value = "料号")
    private String partId;
    @ApiModelProperty(value = "工单")
    private String moId;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "批量数")
    private Integer lotSize;
    @ApiModelProperty(value = "投入数")
    private Integer inputQty;
    @ApiModelProperty(value = "产出数")
    private Integer outputQty;
    @ApiModelProperty(value = "线别")
    private String lineId;
    @ApiModelProperty(value = "作业群组")
    private String workgroupId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "进工序时间")
    private Date inTime;
    @ApiModelProperty(value = "出工序时间")
    private Date outTime;
    @ApiModelProperty(value = "职员信息")
    private String staffId;
    @ApiModelProperty(value = "强制推送到工序")
    private String nextProcessId;
    @ApiModelProperty(value = "进线时间")
    private Date inlineTime;
    @ApiModelProperty(value = "出线时间")
    private Date outlineTime;
    @ApiModelProperty(value = "使用途程")
    private String routeId;
    @ApiModelProperty(value = "检验批号")
    private String qaNo;
    @ApiModelProperty(value = "检验结果")
    private String qaResult;
    @ApiModelProperty(value = "结批号")
    private String batchNo;
    @ApiModelProperty(value = "栈板号")
    private String palletNo;
    @ApiModelProperty(value = "外箱/周转箱号")
    private String cartonNo;
    @ApiModelProperty(value = "盒号/脆盘号")
    private String boxNo;
    @ApiModelProperty(value = "工作状态")
    private Integer wipFlag;
    @ApiModelProperty(value = "判断是否不良")
    private Integer forkFlag;
    @ApiModelProperty(value = "目前工序")
    private String wipNowProcess;
    @ApiModelProperty(value = "下一工序")
    private String wipNextProcess;

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

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getMoId() {
        return moId;
    }
    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getLotSize() {
        return lotSize;
    }
    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

    public Integer getInputQty() {
        return inputQty;
    }
    public void setInputQty(Integer inputQty) {
        this.inputQty = inputQty;
    }

    public Integer getOutputQty() {
        return outputQty;
    }
    public void setOutputQty(Integer outputQty) {
        this.outputQty = outputQty;
    }

    public String getLineId() {
        return lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getWorkgroupId() {
        return workgroupId;
    }
    public void setWorkgroupId(String workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getInTime() {
        return inTime;
    }
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getNextProcessId() {
        return nextProcessId;
    }
    public void setNextProcessId(String nextProcessId) {
        this.nextProcessId = nextProcessId;
    }

    public Date getInlineTime() {
        return inlineTime;
    }
    public void setInlineTime(Date inlineTime) {
        this.inlineTime = inlineTime;
    }

    public Date getOutlineTime() {
        return outlineTime;
    }
    public void setOutlineTime(Date outlineTime) {
        this.outlineTime = outlineTime;
    }

    public String getRouteId() {
        return routeId;
    }
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getQaNo() {
        return qaNo;
    }
    public void setQaNo(String qaNo) {
        this.qaNo = qaNo;
    }

    public String getQaResult() {
        return qaResult;
    }
    public void setQaResult(String qaResult) {
        this.qaResult = qaResult;
    }

    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getPalletNo() {
        return palletNo;
    }
    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    public String getCartonNo() {
        return cartonNo;
    }
    public void setCartonNo(String cartonNo) {
        this.cartonNo = cartonNo;
    }

    public String getBoxNo() {
        return boxNo;
    }
    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public Integer getWipFlag() {
        return wipFlag;
    }
    public void setWipFlag(Integer wipFlag) {
        this.wipFlag = wipFlag;
    }

    public Integer getForkFlag() {
        return forkFlag;
    }
    public void setForkFlag(Integer forkFlag) {
        this.forkFlag = forkFlag;
    }

    public String getWipNowProcess() {
        return wipNowProcess;
    }
    public void setWipNowProcess(String wipNowProcess) {
        this.wipNowProcess = wipNowProcess;
    }

    public String getWipNextProcess() {
        return wipNextProcess;
    }
    public void setWipNextProcess(String wipNextProcess) {
        this.wipNextProcess = wipNextProcess;
    }



}