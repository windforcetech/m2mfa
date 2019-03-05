package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDef;
import com.m2micro.m2mfa.base.entity.BaseQualitySolutionDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/3/4 11:44
 * @Description:
 */
@ApiModel(description = "校检方案详情")
@Data
public class QualitySolutionDescInfo {
    @ApiModelProperty("检验方案主档")
    private BaseQualitySolutionDesc baseQualitySolutionDesc;
    @ApiModelProperty("检验方案明细")
    private List<BaseQualitySolutionDef> baseQualitySolutionDefs;
    @ApiModelProperty("抽样方案下拉框")
    private List<AqlDescSelect> aqlDescSelects;
}
