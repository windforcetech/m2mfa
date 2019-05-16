package com.m2micro.m2mfa.mo.model;


import com.m2micro.m2mfa.mo.entity.MesMoBom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description="新增工单需要的信息")
@Data
public class MesMoDescBomModel {
    @ApiModelProperty(value = "工单主档综合信息")
    private MesMoDescModel mesMoDescModel;
    @ApiModelProperty(value = "工单料表")
    private List<MesMoBom> mesMoBoms;
}
