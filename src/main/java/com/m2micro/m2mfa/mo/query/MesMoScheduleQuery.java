package com.m2micro.m2mfa.mo.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/3 15:14
 * @Description:
 */
@ApiModel(description = "排产单查询参数")
@Data
public class MesMoScheduleQuery extends Query {
    @ApiModelProperty(value = "执行状态")
    private String flag;
}
