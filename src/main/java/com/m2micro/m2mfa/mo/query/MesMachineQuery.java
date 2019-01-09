package com.m2micro.m2mfa.mo.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "排产单机台参数")
public class MesMachineQuery   extends Query {
    @ApiModelProperty(value = "机台编号")
    private String machinCode;

    public String getMachinCode() {
        return machinCode;
    }

    public void setMachinCode(String machinCode) {
        this.machinCode = machinCode;
    }
}
