package com.m2micro.m2mfa.barcode.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("分页查询打印申请")
public class PrintApplyQuery extends Query {
    @ApiModelProperty("打印状态")
    private int flag;
    @ApiModelProperty("开始日期")
    private String startDate;
    @ApiModelProperty("结束日期")
    private String endDate;
}
