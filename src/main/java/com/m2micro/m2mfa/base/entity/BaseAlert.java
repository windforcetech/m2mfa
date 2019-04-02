package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 预警方式设定
 * @author chenshuhong
 * @since 2019-04-02
 */
@Entity
@ApiModel(value="BaseAlert对象", description="预警方式设定")
public class BaseAlert extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String alertId;
    private String sourceCategory;
    private String sourceId;
    private String sendMaintain;
    private String sendManagement;
    private String sendTechnology;
    private String sendBusiness;
    private String sendPlanner;
    private String sendQa;
    private String sendWarehouse;
    private String sendBuyer;
    private String sendEngineer;
    private String sendPe;
    private Integer again;
    private Integer intervalHour;
    private Integer liveBulletin;
    private Date excludeStart;
    private Date excludeEnd;
    private Date delayTime;
    private Integer sendSms;
    private Integer sendMail;
    private Integer showKanban;
    private Boolean enabled;
    private String description;

    public String getAlertId() {
        return alertId;
    }
    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getSourceCategory() {
        return sourceCategory;
    }
    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSendMaintain() {
        return sendMaintain;
    }
    public void setSendMaintain(String sendMaintain) {
        this.sendMaintain = sendMaintain;
    }

    public String getSendManagement() {
        return sendManagement;
    }
    public void setSendManagement(String sendManagement) {
        this.sendManagement = sendManagement;
    }

    public String getSendTechnology() {
        return sendTechnology;
    }
    public void setSendTechnology(String sendTechnology) {
        this.sendTechnology = sendTechnology;
    }

    public String getSendBusiness() {
        return sendBusiness;
    }
    public void setSendBusiness(String sendBusiness) {
        this.sendBusiness = sendBusiness;
    }

    public String getSendPlanner() {
        return sendPlanner;
    }
    public void setSendPlanner(String sendPlanner) {
        this.sendPlanner = sendPlanner;
    }

    public String getSendQa() {
        return sendQa;
    }
    public void setSendQa(String sendQa) {
        this.sendQa = sendQa;
    }

    public String getSendWarehouse() {
        return sendWarehouse;
    }
    public void setSendWarehouse(String sendWarehouse) {
        this.sendWarehouse = sendWarehouse;
    }

    public String getSendBuyer() {
        return sendBuyer;
    }
    public void setSendBuyer(String sendBuyer) {
        this.sendBuyer = sendBuyer;
    }

    public String getSendEngineer() {
        return sendEngineer;
    }
    public void setSendEngineer(String sendEngineer) {
        this.sendEngineer = sendEngineer;
    }

    public String getSendPe() {
        return sendPe;
    }
    public void setSendPe(String sendPe) {
        this.sendPe = sendPe;
    }

    public Integer getAgain() {
        return again;
    }
    public void setAgain(Integer again) {
        this.again = again;
    }

    public Integer getIntervalHour() {
        return intervalHour;
    }
    public void setIntervalHour(Integer intervalHour) {
        this.intervalHour = intervalHour;
    }

    public Integer getLiveBulletin() {
        return liveBulletin;
    }
    public void setLiveBulletin(Integer liveBulletin) {
        this.liveBulletin = liveBulletin;
    }

    public Date getExcludeStart() {
        return excludeStart;
    }
    public void setExcludeStart(Date excludeStart) {
        this.excludeStart = excludeStart;
    }

    public Date getExcludeEnd() {
        return excludeEnd;
    }
    public void setExcludeEnd(Date excludeEnd) {
        this.excludeEnd = excludeEnd;
    }

    public Date getDelayTime() {
        return delayTime;
    }
    public void setDelayTime(Date delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getSendSms() {
        return sendSms;
    }
    public void setSendSms(Integer sendSms) {
        this.sendSms = sendSms;
    }

    public Integer getSendMail() {
        return sendMail;
    }
    public void setSendMail(Integer sendMail) {
        this.sendMail = sendMail;
    }

    public Integer getShowKanban() {
        return showKanban;
    }
    public void setShowKanban(Integer showKanban) {
        this.showKanban = showKanban;
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