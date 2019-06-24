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
@Data
@ApiModel(description = "客户基本资料档")
public class BaseCustomerQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "简称")
    private String name;
    @ApiModelProperty(value = "类型")
    private String category;
    @ApiModelProperty(value = "全称")
    private String fullname;
    @ApiModelProperty(value = "简码")
    private String abbreviation;
    @ApiModelProperty(value = "区域")
    private String area;
    @ApiModelProperty(value = "电话号码")
    private String telephone;
    @ApiModelProperty(value = "传真号码")
    private String fax;
    @ApiModelProperty(value = "公司主页")
    private String web;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

}
