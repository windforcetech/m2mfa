package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description = "员工排班查询参数")
@Data
public class BaseStaffshiftQuery extends Query {
    @ApiModelProperty(value = "部门id")
    private String departmentID;

    @ApiModelProperty(value = "员工主键")
    private String staffId;

    @ApiModelProperty(value = "班别代码")
    private String shiftId;

}
