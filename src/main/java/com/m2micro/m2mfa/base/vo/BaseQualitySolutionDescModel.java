package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/2/19 10:02
 * @Description:
 */
@Data
@ApiModel(description="检验方案需要的数据")
@EqualsAndHashCode
public class BaseQualitySolutionDescModel {
    @ApiModelProperty(value = "检验方案主档")
    private BaseQualitySolutionDesc baseQualitySolutionDesc;
    @ApiModelProperty(value = "检验方案明细")
    private List<BaseQualitySolutionDef> baseQualitySolutionDefs;

}
