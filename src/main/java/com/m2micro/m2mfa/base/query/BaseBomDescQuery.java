package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@Data
@ApiModel(description = "物料清单查询参数")
public class BaseBomDescQuery extends Query {
    @ApiModelProperty(value = "主键")
    private String bomId;
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "版本号")
    private Integer version;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "是否发放")
    private Boolean checkFlag;
    @ApiModelProperty(value = "是否有效")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "料件分类")
    private String partsCategory;


}
