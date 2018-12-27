package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @Auther: liaotao
 * @Date: 2018/12/5 11:29
 * @Description:
 */
@ApiModel(description = "料件基本资料")
public class BasePartsQuery extends Query {
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String name;
    @ApiModelProperty(value = "规格")
    private String spec;
    @ApiModelProperty(value = "物料来源")
    private String source;
    @ApiModelProperty(value = "类型")
    private String category;

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
