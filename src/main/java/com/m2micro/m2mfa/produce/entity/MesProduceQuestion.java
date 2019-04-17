package com.m2micro.m2mfa.produce.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产过程问题
 * @author chenshuhong
 * @since 2019-04-02
 */
@Entity
@ApiModel(value="MesProduceQuestion对象", description="生产过程问题")
public class MesProduceQuestion extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String questionId;
    private String soure;
    private String soureId;
    private String reason;
    private Date ariseTime;
    private String questionInfo;
    private String moldId;
    private String machineId;
    private String scheduleId;
    private String reporterId;
    private String handlerId;
    private String handleMethod;
    private Date handleTime;
    private Boolean handleState;

    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSoure() {
        return soure;
    }
    public void setSoure(String soure) {
        this.soure = soure;
    }

    public String getSoureId() {
        return soureId;
    }
    public void setSoureId(String soureId) {
        this.soureId = soureId;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getAriseTime() {
        return ariseTime;
    }
    public void setAriseTime(Date ariseTime) {
        this.ariseTime = ariseTime;
    }

    public String getQuestionInfo() {
        return questionInfo;
    }
    public void setQuestionInfo(String questionInfo) {
        this.questionInfo = questionInfo;
    }

    public String getMoldId() {
        return moldId;
    }
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getReporterId() {
        return reporterId;
    }
    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getHandlerId() {
        return handlerId;
    }
    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandleMethod() {
        return handleMethod;
    }
    public void setHandleMethod(String handleMethod) {
        this.handleMethod = handleMethod;
    }

    public Date getHandleTime() {
        return handleTime;
    }
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Boolean getHandleState() {
        return handleState;
    }
    public void setHandleState(Boolean handleState) {
        this.handleState = handleState;
    }



}