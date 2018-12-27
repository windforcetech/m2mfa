package com.m2micro.m2mfa.pr.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    @Size(max=32,message = "料件id长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String partId;
    @ApiModelProperty(value = "管制方式")
    @NotNull(message="管制方式不能为空。",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "料件id长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String controlInformation;
    @ApiModelProperty(value = "工艺主键")
    @NotNull(message="管制方式不能为空。",groups = {AddGroup.class, UpdateGroup.class})
    @Size(max=32,message = "料件id长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    private String routeId;

    @ApiModelProperty(value = "投入工序")
    private String inputProcessId;

    @ApiModelProperty(value = "产出工序")
    private String outputProcessId;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @ApiModelProperty(value = "描述")

    private String description;

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



}