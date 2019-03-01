package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseSymptom;
import com.m2micro.m2mfa.base.node.SelectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/28 14:17
 * @Description:
 */
@Data
@ApiModel(description="不良原因详情")
public class BaseSymptomModel {
    @ApiModelProperty(value = "不良原因信息")
    BaseSymptom baseSymptom;
    @ApiModelProperty(value = "不良原因分类下拉选项")
    List<SelectNode> list;
}
