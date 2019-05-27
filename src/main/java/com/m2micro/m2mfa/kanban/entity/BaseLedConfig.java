package com.m2micro.m2mfa.kanban.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author chenshuhong
 * @since 2019-05-27
 */
@Entity
@ApiModel(value="BaseLedConfig对象", description="")
public class BaseLedConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    @Id
    private String configId;
    @ApiModelProperty(value = "看板ip")
    private String elemen;
    @ApiModelProperty(value = "摆放车间")
    private String workshop;
    @ApiModelProperty(value = "摆放位置")
    private String position;
    @ApiModelProperty(value = "切换画面频率")
    private Integer rate;
    @ApiModelProperty(value = "每页显示行数")
    private Integer rowPage;
    @ApiModelProperty(value = "机台展示列数 ")
    private Integer colQty;
    @ApiModelProperty(value = "机台展示行数")
    private Integer rowQty;
    @ApiModelProperty(value = "机台对应的列表id")
    private String machineList;
    @Transient
    @ApiModelProperty(value = "机台对应的列表数据")
    private List<BaseMachineList> baseMachineLists;

    public String getConfigId() {
        return configId;
    }
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getElemen() {
        return elemen;
    }
    public void setElemen(String elemen) {
        this.elemen = elemen;
    }

    public String getWorkshop() {
        return workshop;
    }
    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getRate() {
        return rate;
    }
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRowPage() {
        return rowPage;
    }
    public void setRowPage(Integer rowPage) {
        this.rowPage = rowPage;
    }

    public Integer getColQty() {
        return colQty;
    }
    public void setColQty(Integer colQty) {
        this.colQty = colQty;
    }

    public Integer getRowQty() {
        return rowQty;
    }
    public void setRowQty(Integer rowQty) {
        this.rowQty = rowQty;
    }

    public String getMachineList() {
        return machineList;
    }
    public void setMachineList(String machineList) {
        this.machineList = machineList;
    }

    public List<BaseMachineList> getBaseMachineLists() {
        return baseMachineLists;
    }

    public void setBaseMachineLists(List<BaseMachineList> baseMachineLists) {
        this.baseMachineLists = baseMachineLists;
    }
}
