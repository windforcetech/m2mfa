package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description="工位基本档查询参数")
public class BaseStaffQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "是否有效 ")
    private boolean enabled;
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

    @ApiModelProperty(value = "部门ids")
    private List<String> departmentIds;

    public List<String> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
