package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/16 13:28
 * @Description:
 */
@ApiModel(description="pad端登陆信息")
@Data
public class LoginData {
    @ApiModelProperty(value = "token令牌")
    private String tokenId;
    @ApiModelProperty(value = "排产单")
    private List<PadScheduleModel> schedules;
}
