package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
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
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String routeId;
    @ApiModelProperty(value = "工艺代码")
    @NotNull(message="工艺代码不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String routeNo;
    @ApiModelProperty(value = "工艺名称")
    @NotNull(message="工艺名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    private String routeName;
    @ApiModelProperty(value = "投入工序id")

    private String inputProcess;
    @ApiModelProperty(value = "产出工序id")

    private String outputProcess;
    @ApiModelProperty(value = "有效否")
    @NotNull(message="有效否",groups = {AddGroup.class, UpdateGroup.class})
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "投入工序名称")
    @Transient
    private String inputProcessName;

    @ApiModelProperty(value = "产出工序名称")
    @Transient
    private String outputProcessName;

    public BaseRouteDesc() {
    }

    public BaseRouteDesc(@NotEmpty(message = "主键不能为空", groups = {UpdateGroup.class}) String routeId, @NotNull(message = "工艺代码不能为空", groups = {AddGroup.class, UpdateGroup.class}) String routeNo, @NotNull(message = "工艺名称不能为空", groups = {AddGroup.class, UpdateGroup.class}) String routeName, String inputProcess, String outputProcess, @NotNull(message = "有效否", groups = {AddGroup.class, UpdateGroup.class}) Boolean enabled, String description, String inputProcessName, String outputProcessName) {
        this.routeId = routeId;
        this.routeNo = routeNo;
        this.routeName = routeName;
        this.inputProcess = inputProcess;
        this.outputProcess = outputProcess;
        this.enabled = enabled;
        this.description = description;
        this.inputProcessName = inputProcessName;
        this.outputProcessName = outputProcessName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getInputProcessName() {
        return inputProcessName;
    }

    public void setInputProcessName(String inputProcessName) {
        this.inputProcessName = inputProcessName;
    }

    public String getOutputProcessName() {
        return outputProcessName;
    }

    public void setOutputProcessName(String outputProcessName) {
        this.outputProcessName = outputProcessName;
    }
}