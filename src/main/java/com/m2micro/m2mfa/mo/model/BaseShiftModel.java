package com.m2micro.m2mfa.mo.model;

import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Auther: liaotao
 * @Date: 2019/1/3 17:14
 * @Description:
 */
@ApiModel(description="班别信息")
@Data
public class BaseShiftModel {

    @ApiModelProperty(value = "主键")
    private String shiftId;
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
}
