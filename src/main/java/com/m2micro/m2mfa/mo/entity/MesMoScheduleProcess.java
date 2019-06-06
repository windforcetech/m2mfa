package com.m2micro.m2mfa.mo.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
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
    @NotEmpty(message="生产排程工序主键不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键")
    @Id
    private String id;
    @NotEmpty(message="排程主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "排程主键")
    private String scheduleId;
    @NotEmpty(message="工序主键不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "工序")
    private String processId;

    @Transient
    @ApiModelProperty(value = "工序名称")
    private String processName;

    @Transient
    @ApiModelProperty(value = "类型")
    private String category;

    @ApiModelProperty(value = "作业工位")
    private String stationId;

    @Transient
    @ApiModelProperty(value = "作业工位名称")
    private String stationName;


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
    @ApiModelProperty(value = "上次模数")
    private Integer oldMolds;
    @ApiModelProperty(value = "产出数")
    private Integer outputQty;

    @ApiModelProperty(value = "模具主键")
    private String moldId;

    @Transient
    @ApiModelProperty(value = "模具名称")
    private String moldName;


    @ApiModelProperty(value = "模仁主键")
    private String mandrelId;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Integer getOldMolds() {
        return oldMolds;
    }

    public void setOldMolds(Integer oldMolds) {
        this.oldMolds = oldMolds;
    }

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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
