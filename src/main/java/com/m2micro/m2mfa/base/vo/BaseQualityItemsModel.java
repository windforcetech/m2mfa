package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BaseQualityItems;
import com.m2micro.m2mfa.base.node.SelectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/29 10:09
 * @Description:
 */
@Data
@ApiModel(description="检验项目详情需要的数据")
public class BaseQualityItemsModel {
    @ApiModelProperty(value = "检验项目信息")
    private BaseQualityItems baseQualityItems;
    @ApiModelProperty(value = "量具名称")
    private List<SelectNode> gauge;
    @ApiModelProperty(value = "检验值类型")
    private List<SelectNode> category;
    @ApiModelProperty(value = "检验单位")
    private List<SelectNode> limitUnit;
}
