package com.m2micro.m2mfa.base.vo;

import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.awt.*;
import java.util.ArrayList;

@Data
@Builder
@ApiModel(value="BaseStaffQueryObj", description="查询职员条件")
public class BaseStaffQueryObj extends Query {

    @ApiModelProperty(value = "工号")
    private String code;

    @ApiModelProperty(value = "姓名")
    private String  name;

    @ApiModelProperty(value = "所属部门Id")
    private String departmentId;

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
    private String icCard;

    @ApiModelProperty(value = "是否有效 ")
    private Boolean enabled;
}
