package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/28 13:46
 * @Description:
 */
@ApiModel(description="不良原因查询参数")
@Data
public class BaseSymptomQuery extends Query {

    @ApiModelProperty(value = "不良原因代码")
    private String symptomCode;
    @ApiModelProperty(value = "不良原因名称")
    private String symptomName;
}
