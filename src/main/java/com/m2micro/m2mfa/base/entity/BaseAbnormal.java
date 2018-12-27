package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 异常项目档
 * @author liaotao
 * @since 2018-12-27
 */
@Entity
@ApiModel(value="BaseAbnormal对象", description="异常项目档")
public class BaseAbnormal extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    private String abnormalId;
    @ApiModelProperty(value = "编号")
    private String abnormalCode;
    @ApiModelProperty(value = "名称")
    private String abnormalName;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "发送异常信息")
    private String sendMessage;
    @ApiModelProperty(value = "作业人员可自行解除")
    private Boolean voluntarilyRemove;
    @ApiModelProperty(value = "自动产生对应单据")
    private Boolean autoProduceDoc;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getAbnormalId() {
        return abnormalId;
    }
    public void setAbnormalId(String abnormalId) {
        this.abnormalId = abnormalId;
    }

    public String getAbnormalCode() {
        return abnormalCode;
    }
    public void setAbnormalCode(String abnormalCode) {
        this.abnormalCode = abnormalCode;
    }

    public String getAbnormalName() {
        return abnormalName;
    }
    public void setAbnormalName(String abnormalName) {
        this.abnormalName = abnormalName;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getSendMessage() {
        return sendMessage;
    }
    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Boolean getVoluntarilyRemove() {
        return voluntarilyRemove;
    }
    public void setVoluntarilyRemove(Boolean voluntarilyRemove) {
        this.voluntarilyRemove = voluntarilyRemove;
    }

    public Boolean getAutoProduceDoc() {
        return autoProduceDoc;
    }
    public void setAutoProduceDoc(Boolean autoProduceDoc) {
        this.autoProduceDoc = autoProduceDoc;
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