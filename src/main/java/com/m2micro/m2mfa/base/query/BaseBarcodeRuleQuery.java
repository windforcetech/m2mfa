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
@Data
@ApiModel(description="条形码规则查询参数")
public class BaseBarcodeRuleQuery extends Query {
    @ApiModelProperty(value = "条码编号")
    private String ruleCode;
    @ApiModelProperty(value = "名称")
    private String ruleName;

}
