package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @Auther: liaotao
 * @Date: 2018/12/3 16:38
 * @Description:
 */
@ApiModel(description = "机台查询参数")
public class BaseMachineQuery extends Query {

    @ApiModelProperty("设备编号")
    private String code;
    @ApiModelProperty("设备名称")
    private String name;
    @ApiModelProperty("状态")
    private String flag;
    @ApiModelProperty("资产部门")
    private String departmentId;
    @ApiModelProperty("摆放位置")
    private String placement;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
