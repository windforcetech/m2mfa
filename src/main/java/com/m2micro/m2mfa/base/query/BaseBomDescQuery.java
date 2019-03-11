package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@ApiModel(description = "物料清单查询参数")
public class BaseBomDescQuery extends Query {
    @ApiModelProperty(value = "主键")
    private String bomId;
    @ApiModelProperty(value = "料件编号")
    private String partId;
    @ApiModelProperty(value = "版本号")
    private String version;
    @ApiModelProperty(value = "类型")
    private String category;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
