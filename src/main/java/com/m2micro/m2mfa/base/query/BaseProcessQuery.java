package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "工序基本资料")
public class BaseProcessQuery  extends Query {


    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "工序类型")
    private String category;

    public BaseProcessQuery() {
    }
    public BaseProcessQuery(String processCode, String processName, String category) {
        this.processCode = processCode;
        this.processName = processName;
        this.category = category;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
