package com.m2micro.m2mfa.mo.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 14:54
 * @Description:
 */
@ApiModel(description = "排产机台查询参数")
@Data
public class MesMoScheduleMachineQuery extends Query {
    @ApiModelProperty(value = "机台编号")
    private String code;
    @ApiModelProperty(value = "左侧树选中的机台id",required = true)
    private String machineId;
}
