package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description="职员基本档查询参数")
@Data
public class BaseStaffQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "职位id")
    private String dutyId;

    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "离职")
    private Boolean isDimission;

    @ApiModelProperty(value = "卡号")
    private Boolean icCard;

    @ApiModelProperty(value = "是否有效 ")
    private Boolean enabled;

    @ApiModelProperty(value = "部门ids")
    private List<String> departmentIds;


//    @ApiModelProperty(value = "岗位ids")
//    public List<String> getDutyIds() {
//        return dutyIds;
//    }
//
//    public void setDutyIds(List<String> dutyIds) {
//        this.dutyIds = dutyIds;
//    }

//    @ApiModelProperty(value = "岗位类型")
//    private List<String> dutyIds;


}
