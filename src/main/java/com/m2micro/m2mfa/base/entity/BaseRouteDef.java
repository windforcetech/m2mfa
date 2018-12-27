package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产途程单身
 * @author chenshuhong
 * @since 2018-12-17
 */
@Entity
@ApiModel(value="BaseRouteDef对象", description="生产途程单身")
public class BaseRouteDef extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键guid")
    @Id
    @NotEmpty(message="主键不能为空",groups = {UpdateGroup.class})
    private String routeDefId;
    @ApiModelProperty(value = "工艺主表id")
    private String routeId;
    @ApiModelProperty(value = "步骤")
    private Integer setp;
    @ApiModelProperty(value = "工序")

    private String processId;
    @ApiModelProperty(value = "下一工序")
    private String nextprocessId;
    @ApiModelProperty(value = "不良工序工序")
    private String failprocessId;
    @ApiModelProperty(value = "允许跳过")
    private Integer jump;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

    public String getRouteDefId() {
        return routeDefId;
    }
    public void setRouteDefId(String routeDefId) {
        this.routeDefId = routeDefId;
    }

    public String getRouteId() {
        return routeId;
    }
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Integer getSetp() {
        return setp;
    }
    public void setSetp(Integer setp) {
        this.setp = setp;
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getNextprocessId() {
        return nextprocessId;
    }
    public void setNextprocessId(String nextprocessId) {
        this.nextprocessId = nextprocessId;
    }

    public String getFailprocessId() {
        return failprocessId;
    }
    public void setFailprocessId(String failprocessId) {
        this.failprocessId = failprocessId;
    }

    public Integer getJump() {
        return jump;
    }
    public void setJump(Integer jump) {
        this.jump = jump;
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