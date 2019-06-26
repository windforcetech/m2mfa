package com.m2micro.m2mfa.pr.entity;


import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 料件途程设定主档
 * @author liaotao
 * @since 2018-12-19
 */
@Entity
@ApiModel(value="MesPartRoute对象", description="料件途程设定主档")
public class MesPartRoute extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @Id
    @NotNull(message="主键不能为空。",groups = {AddGroup.class, UpdateGroup.class})
    private String partRouteId;
    @ApiModelProperty(value = "料件id")
    @NotNull(message="料件id不能为空。",groups = {AddGroup.class, UpdateGroup.class})

    private String partId;
    @ApiModelProperty(value = "管制方式")
    @NotNull(message="管制方式不能为空。",groups = {AddGroup.class, UpdateGroup.class})

    private String controlInformation;

    @Transient
    @ApiModelProperty(value = "管制方式名称")
    private String controlInformationName;


    @ApiModelProperty(value = "工艺主键")

    private String routeId;

    @ApiModelProperty(value = "投入工序")
    private String inputProcessId;

    @ApiModelProperty(value = "产出工序")
    private String outputProcessId;

    @ApiModelProperty(value = "有效否")
    private Integer enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @Transient
    @ApiModelProperty(value = "途程名称")
    private String touteName;

    @Transient
    @ApiModelProperty(value = "料件编号")
    private String partNo;

    @Transient
    @ApiModelProperty(value = "投入工序名称")
    private String inputProcessIdName;

    @Transient
    @ApiModelProperty(value = "产出工序名称")
    private String outputProcessIdName;

    public MesPartRoute(@NotNull(message = "主键不能为空。", groups = {AddGroup.class, UpdateGroup.class}) String partRouteId, @NotNull(message = "料件id不能为空。", groups = {AddGroup.class, UpdateGroup.class})   String partId, @NotNull(message = "管制方式不能为空。", groups = {AddGroup.class, UpdateGroup.class})   String controlInformation, String controlInformationName,
                        String routeId, String inputProcessId, String outputProcessId, Integer enabled, String description, String touteName, String partNo, String inputProcessIdName, String outputProcessIdName) {
        this.partRouteId = partRouteId;
        this.partId = partId;
        this.controlInformation = controlInformation;
        this.controlInformationName = controlInformationName;
        this.routeId = routeId;
        this.inputProcessId = inputProcessId;
        this.outputProcessId = outputProcessId;
        this.enabled = enabled;
        this.description = description;
        this.touteName = touteName;
        this.partNo = partNo;
        this.inputProcessIdName = inputProcessIdName;
        this.outputProcessIdName = outputProcessIdName;
    }

    public MesPartRoute() {
    }

    public MesPartRoute(String partNo) {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPartRouteId() {
        return partRouteId;
    }

    public void setPartRouteId(String partRouteId) {
        this.partRouteId = partRouteId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getControlInformation() {
        return controlInformation;
    }

    public void setControlInformation(String controlInformation) {
        this.controlInformation = controlInformation;
    }

    public String getControlInformationName() {
        return controlInformationName;
    }

    public void setControlInformationName(String controlInformationName) {
        this.controlInformationName = controlInformationName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getInputProcessId() {
        return inputProcessId;
    }

    public void setInputProcessId(String inputProcessId) {
        this.inputProcessId = inputProcessId;
    }

    public String getOutputProcessId() {
        return outputProcessId;
    }

    public void setOutputProcessId(String outputProcessId) {
        this.outputProcessId = outputProcessId;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTouteName() {
        return touteName;
    }

    public void setTouteName(String touteName) {
        this.touteName = touteName;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getInputProcessIdName() {
        return inputProcessIdName;
    }

    public void setInputProcessIdName(String inputProcessIdName) {
        this.inputProcessIdName = inputProcessIdName;
    }

    public String getOutputProcessIdName() {
        return outputProcessIdName;
    }

    public void setOutputProcessIdName(String outputProcessIdName) {
        this.outputProcessIdName = outputProcessIdName;
    }
}
