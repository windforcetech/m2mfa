package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2019/3/4 10:14
 * @Description:
 */
@ApiModel(description = "检验方案查询参数")
@Data
public class BaseQualitySolutionDescQuery extends Query {
    @ApiModelProperty(value = "方案编号")
    private String solutionCode;
    @ApiModelProperty(value = "方案名称")
    private String solutionName;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "抽检方案")
    private String aqlId;

}
