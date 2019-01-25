package com.m2micro.m2mfa.mo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/3 17:05
 * @Description:
 */
@ApiModel(description="排产单新增需要的信息")
@Data
public class MesMoScheduleInfoModel {

    List<BaseShiftModel> baseShiftModels;
}
