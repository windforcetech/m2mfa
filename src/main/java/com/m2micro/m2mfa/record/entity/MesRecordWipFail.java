package com.m2micro.m2mfa.record.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 在制不良记录表
 * @author chenshuhong
 * @since 2019-05-06
 */
@Entity
@ApiModel(value="MesRecordWipFail对象", description="在制不良记录表")
public class MesRecordWipFail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "箱号")
    private String serialNumber;
    @ApiModelProperty(value = "工单")
    private String moId;
    @ApiModelProperty(value = "排产单id")
    private String scheduleId;
    @ApiModelProperty(value = "料号")
    private String partId;
    @ApiModelProperty(value = "目标工序")
    private String targetProcessId;
    @ApiModelProperty(value = "目标工位")
    private String targetStationId;
    @ApiModelProperty(value = "当前工序")
    private String nowProcessId;
    @ApiModelProperty(value = "当前工位")
    private String nowStationId;
    @ApiModelProperty(value = "操作员")
    private String staffId;
    @ApiModelProperty(value = "处理状态")
    private Integer repairFlag;
    @ApiModelProperty(value = "不良现象代码")
    private String defectCode;
    @ApiModelProperty(value = "不良数量")
    private Integer failQty;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapQty;
    @ApiModelProperty(value = "回流数量")
    private Integer backflowQty;
    @ApiModelProperty(value = "完成时间")
    private Date closeOn;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMoId() {
        return moId;
    }
    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getTargetProcessId() {
        return targetProcessId;
    }
    public void setTargetProcessId(String targetProcessId) {
        this.targetProcessId = targetProcessId;
    }

    public String getTargetStationId() {
        return targetStationId;
    }
    public void setTargetStationId(String targetStationId) {
        this.targetStationId = targetStationId;
    }

    public String getNowProcessId() {
        return nowProcessId;
    }
    public void setNowProcessId(String nowProcessId) {
        this.nowProcessId = nowProcessId;
    }

    public String getNowStationId() {
        return nowStationId;
    }
    public void setNowStationId(String nowStationId) {
        this.nowStationId = nowStationId;
    }

    public String getStaffId() {
        return staffId;
    }
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Integer getRepairFlag() {
        return repairFlag;
    }
    public void setRepairFlag(Integer repairFlag) {
        this.repairFlag = repairFlag;
    }

    public String getDefectCode() {
        return defectCode;
    }
    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public Integer getFailQty() {
        return failQty;
    }
    public void setFailQty(Integer failQty) {
        this.failQty = failQty;
    }

    public Integer getScrapQty() {
        return scrapQty;
    }
    public void setScrapQty(Integer scrapQty) {
        this.scrapQty = scrapQty;
    }

    public Integer getBackflowQty() {
        return backflowQty;
    }
    public void setBackflowQty(Integer backflowQty) {
        this.backflowQty = backflowQty;
    }

    public Date getCloseOn() {
        return closeOn;
    }
    public void setCloseOn(Date closeOn) {
        this.closeOn = closeOn;
    }



}