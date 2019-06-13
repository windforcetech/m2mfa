package com.m2micro.m2mfa.report.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工单工序信息
 * @author chenshuhong
 * @since 2019-06-12
 */
@Entity
@ApiModel(value="MesProcessInfo对象", description="工单工序信息")
public class MesProcessInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String mesProcessInfoId;
    @ApiModelProperty(value = "工单ID")
    private String moId;
    @ApiModelProperty(value = "排产单id")
    private String scheduleId;
    @ApiModelProperty(value = "料件ID")
    private String partId;
    @ApiModelProperty(value = "工序id")
    private String processId;
    @ApiModelProperty(value = "产量")
    private Integer outputQty;
    @ApiModelProperty(value = "是否产出工序")
    private Integer flag;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "不良数量")
    private Integer failQty;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapQty;

    public String getMesProcessInfoId() {
        return mesProcessInfoId;
    }
    public void setMesProcessInfoId(String mesProcessInfoId) {
        this.mesProcessInfoId = mesProcessInfoId;
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

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Integer getOutputQty() {
        return outputQty;
    }
    public void setOutputQty(Integer outputQty) {
        this.outputQty = outputQty;
    }

    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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



}