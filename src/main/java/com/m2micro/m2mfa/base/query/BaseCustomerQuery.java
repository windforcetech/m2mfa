package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @Auther: liaotao
 * @Date: 2018/12/5 11:29
 * @Description:
 */
@ApiModel(description = "客户基本资料档")
public class BaseCustomerQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "简称")
    private String name;
    @ApiModelProperty(value = "类型")
    private String category;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
