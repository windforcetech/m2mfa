package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liaotao
 * @Date: 2019/1/29 09:26
 * @Description:
 */
@ApiModel(description = "工序基本资料")
@Data
public class BaseQualityItemsQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String itemCode;
    @ApiModelProperty(value = "名称")
    private String itemName;
    @ApiModelProperty(value = "量具名称")
    private String gauge;
    @ApiModelProperty(value = "是否有效")
    private Boolean enabled;
    @ApiModelProperty(value = "类型")
    private String categoryName;
    @ApiModelProperty(value = "上限")
    private String upperLimit;
    @ApiModelProperty(value = "下限")
    private String centralLimit;
    @ApiModelProperty(value = "标准")
    private String lowerLimit;
    @ApiModelProperty(value = "名称")
    private String limitUnitName;
    @ApiModelProperty(value = "描述")
    private String description;
}
