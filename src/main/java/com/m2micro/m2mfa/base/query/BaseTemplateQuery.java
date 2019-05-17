package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * @Auther: liaotao
 * @Date: 2018/12/17 10:58
 * @Description:
 */
@Data
@ApiModel(description = "标签模板查询对象")
public class BaseTemplateQuery extends Query {
    @ApiModelProperty(value = "编号")
    private String number;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "标签类型")
    private String category;
    @ApiModelProperty(value = "版本")
    private String version;
    @ApiModelProperty(value = "是否有效")
    private Boolean  enabled;

}
