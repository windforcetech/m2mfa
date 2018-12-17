package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产途程单头
 * @author chenshuhong
 * @since 2018-12-17
 */
@Entity
@ApiModel(value="BaseRouteDesc对象", description="生产途程单头")
public class BaseRouteDesc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键guid")
    @Id
    private String routeId;
    @ApiModelProperty(value = "工艺代码")
    private String routeNo;
    @ApiModelProperty(value = "工艺名称")
    private String routeName;
    @ApiModelProperty(value = "投入工序id")
    private String inputProcess;
    @ApiModelProperty(value = "产出工序id")
    private String outputProcess;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getRouteId() {
        return routeId;
    }
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteNo() {
        return routeNo;
    }
    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getInputProcess() {
        return inputProcess;
    }
    public void setInputProcess(String inputProcess) {
        this.inputProcess = inputProcess;
    }

    public String getOutputProcess() {
        return outputProcess;
    }
    public void setOutputProcess(String outputProcess) {
        this.outputProcess = outputProcess;
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