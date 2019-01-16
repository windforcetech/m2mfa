package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
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
    private String tokenId;
    private List<PadScheduleModel> schedules;
}
