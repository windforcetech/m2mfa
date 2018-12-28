package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.base.entity.BaseStation;
import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2018/12/28 14:59
 * @Description:
 */
@ApiModel(description="pad端登陆后初始数据")
@Data
public class InitData {
    @ApiModelProperty(value = "排产单")
    MesMoSchedule mesMoSchedule;
    @ApiModelProperty(value = "待处理工位")
    List<BaseStation> baseStations;
}
