package com.m2micro.m2mfa.mo.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;


/**
 * @Auther: liaotao
 * @Date: 2018/12/10 10:01
 * @Description:
 */
@ApiModel(description = "工单主档查询参数")
public class MesMoDescQuery extends Query {
    @ApiModelProperty(value = "工单号码")
    private String moNumber;
    @ApiModelProperty(value = "工单状态")
    private String closeFlag;

    public String getMoNumber() {
        return moNumber;
    }

    public void setMoNumber(String moNumber) {
        this.moNumber = moNumber;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(String closeFlag) {
        this.closeFlag = closeFlag;
    }
}
