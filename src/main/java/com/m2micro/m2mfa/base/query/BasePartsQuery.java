package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2018/12/5 11:29
 * @Description:
 */
@ApiModel(description = "料件基本资料")
@Data
public class BasePartsQuery extends Query {
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String name;
    @ApiModelProperty(value = "规格")
    private String spec;
    @ApiModelProperty(value = "物料来源")
    private String source;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "类型")
    private String  typesof;
    @ApiModelProperty(value = "是否为工单")
    private boolean  isom;
    @ApiModelProperty(value = "是否为模板")
    private Boolean  isTemplate;
}
