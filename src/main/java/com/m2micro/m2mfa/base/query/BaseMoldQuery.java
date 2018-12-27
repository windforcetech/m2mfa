package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * @Auther: liaotao
 * @Date: 2018/12/3 16:38
 * @Description:
 */
@ApiModel(description = "模具主档参数")
public class BaseMoldQuery extends Query {

    @ApiModelProperty(value = "模具编号")
    private String code;
    @ApiModelProperty(value = "模具名称")
    private String name;
    @ApiModelProperty(value = "所属客户")
    private String customerName;
    @ApiModelProperty(value = "所属客户id")
    private String customerId;
    @ApiModelProperty(value = "模具状态")
    private String flag;
    @ApiModelProperty(value = "模具分类id")
    private String categoryId;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
