package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@Data
@ApiModel(description="包装查询参数")
public class BasePackQuery extends Query {

    @ApiModelProperty(value = "料件编号")
    private String partId;

}
