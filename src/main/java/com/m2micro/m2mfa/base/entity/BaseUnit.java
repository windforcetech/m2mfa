package com.m2micro.m2mfa.base.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import com.m2micro.m2mfa.common.entity.BaseEntity;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liaotao
 * @since 2018-12-20
 */
@Entity
@ApiModel(value="BaseUnit对象", description="")
public class BaseUnit extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键id")
    @Id
    private String unitId;
    @Size(max=32,message = "单位长度不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "单位")
    private String unit;
    @Size(max=32,message = "数据字节不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "数据字节")
    private Integer dataBit;
    @ApiModelProperty(value = "有效否")
    private Integer enabled;
    @Size(max=32,message = "描述不能大于32位",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "描述")
    private String description;

    public String getUnitId() {
        return unitId;
    }
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getDataBit() {
        return dataBit;
    }
    public void setDataBit(Integer dataBit) {
        this.dataBit = dataBit;
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