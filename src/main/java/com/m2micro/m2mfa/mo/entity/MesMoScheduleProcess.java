package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产排程工序
 * @author liaotao
 * @since 2019-01-02
 */
@Entity
@ApiModel(value="MesMoScheduleProcess对象", description="生产排程工序")
public class MesMoScheduleProcess extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @ApiModelProperty(value = "排程主键")
    private String scheduleId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "作业工位")
    private String stationId;
    @ApiModelProperty(value = "预计开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "预计结束时间")
    private Date planEndTime;
    @ApiModelProperty(value = "实际开始时间")
    private Date actualStartTime;
    @ApiModelProperty(value = "实际结束时间")
    private Date actualEndTime;
    @ApiModelProperty(value = "包装配置档")
    private String packId;
    @ApiModelProperty(value = "检验配置档")
    private String qualitySolutionId;
    @ApiModelProperty(value = "啤/次数")
    private Integer beerQty;
    @ApiModelProperty(value = "产出数")
    private Integer outputQty;
    @ApiModelProperty(value = "模具主键")
    private String moldId;
    @ApiModelProperty(value = "模仁主键")
    private String mandrelId;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }
    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }
    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getPackId() {
        return packId;
    }
    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getQualitySolutionId() {
        return qualitySolutionId;
    }
    public void setQualitySolutionId(String qualitySolutionId) {
        this.qualitySolutionId = qualitySolutionId;
    }

    public Integer getBeerQty() {
        return beerQty;
    }
    public void setBeerQty(Integer beerQty) {
        this.beerQty = beerQty;
    }

    public Integer getOutputQty() {
        return outputQty;
    }
    public void setOutputQty(Integer outputQty) {
        this.outputQty = outputQty;
    }

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMandrelId() {
        return mandrelId;
    }
    public void setMandrelId(String mandrelId) {
        this.mandrelId = mandrelId;
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