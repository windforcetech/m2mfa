package com.m2micro.m2mfa.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "BaseStaffDetailObj", description = "职员详情")
@Data
public class BaseStaffDetailObj {

    @ApiModelProperty(value = "职员id")
    private String Id;

    @ApiModelProperty(value = "工号")
    private String code;
    @ApiModelProperty(value = "姓名")
    private String name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "部门")

    private String department;

    @ApiModelProperty(value = "岗位")
    private String duty;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "离职")
    private Boolean isDimission;

    @ApiModelProperty(value = "有效")
    private Boolean enabled;

}
