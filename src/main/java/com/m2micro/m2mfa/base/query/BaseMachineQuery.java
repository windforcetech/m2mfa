package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2018/12/3 16:38
 * @Description:
 */
@Data
@ApiModel(description = "机台查询参数")
public class BaseMachineQuery extends Query {

    @ApiModelProperty("设备编号")
    private String code;
    @ApiModelProperty("设备名称")
    private String name;
    @ApiModelProperty("状态")
    private String flag;
    @ApiModelProperty("归属部门")
    private String departmentId;
    @ApiModelProperty("摆放位置")
    private String placement;
    @ApiModelProperty("固资编号")
    private String assdtId;
    @ApiModelProperty("设备类型")
    private String categoryId;
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("对应维修人员")
    private String maintenanceStaff;
    @ApiModelProperty("对应技术人员")
    private String technicalStaff;
    @ApiModelProperty("对应管理人员")
    private String managerStaff;
    @ApiModelProperty("有效否")
    private Boolean enabled;
    @ApiModelProperty("description")
    private String description;


}
