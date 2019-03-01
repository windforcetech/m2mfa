package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "工艺基本资料")
public class BaseRouteQuery  extends Query {

    @ApiModelProperty(value = "工艺代码")
    private String routeNo;

    @ApiModelProperty(value = "工艺名称 ")
    private String routeName;

    @ApiModelProperty(value = "请求类型 ")
    private String type;

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
