package com.m2micro.m2mfa.mo.model;


import com.m2micro.m2mfa.mo.entity.MesMoBom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description="新增工单选中料件时获取的信息")
@Data
public class BomAndPartInfoModel {
    @ApiModelProperty(value = "料件综合信息")
    private PartsRouteModel partsRouteModel;
    @ApiModelProperty(value = "工单料表")
    private List<MesMoBom> mesMoBoms;
}
