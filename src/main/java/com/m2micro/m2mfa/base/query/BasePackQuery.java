package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description="包装查询参数")
public class BasePackQuery extends Query {
    @ApiModelProperty(value = "料件编号")
    private String partId;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }
}
