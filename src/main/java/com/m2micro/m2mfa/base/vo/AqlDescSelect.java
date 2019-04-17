package com.m2micro.m2mfa.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * @Auther: liaotao
 * @Date: 2019/3/4 11:13
 * @Description:
 */
@ApiModel(description = "抽样方案下拉选项")
@Data
public class AqlDescSelect {
    @ApiModelProperty(value = "抽样方案主键")
    private String aqlId;
    @ApiModelProperty(value = "抽样方案名称")
    private String aqlName;
}
