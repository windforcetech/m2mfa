package com.m2micro.m2mfa.kanban.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

import com.m2micro.m2mfa.base.entity.BaseMachine;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author chenshuhong
 * @since 2019-05-27
 */
@Entity
@ApiModel(value="BaseMachineList对象", description="")
public class BaseMachineList extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String machineListId;
    @ApiModelProperty(value = "看版配置项Id")
    private String configId;
    @ApiModelProperty(value = "机台id")
    private String machineId;
    @ApiModelProperty(value = "机台数据")
    @Transient
    private BaseMachine baseMachine;

    public String getMachineListId() {
        return machineListId;
    }
    public void setMachineListId(String machineListId) {
        this.machineListId = machineListId;
    }

    public String getConfigId() {
        return configId;
    }
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public BaseMachine getBaseMachine() {
        return baseMachine;
    }

    public void setBaseMachine(BaseMachine baseMachine) {
        this.baseMachine = baseMachine;
    }
}
