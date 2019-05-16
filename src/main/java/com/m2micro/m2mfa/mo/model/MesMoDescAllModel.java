package com.m2micro.m2mfa.mo.model;


import com.m2micro.m2mfa.mo.entity.MesMoBom;
import com.m2micro.m2mfa.mo.entity.MesMoDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description="保存工单需要的信息")
@Data
public class MesMoDescAllModel {
    @ApiModelProperty(value = "工单信息表")
    private MesMoDesc mesMoDesc;
    @ApiModelProperty(value = "工单料表")
    private List<MesMoBom> mesMoBoms;
}
