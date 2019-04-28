package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2018/12/5 11:29
 * @Description:
 */
@ApiModel(description = "料件基本资料")
@Data
public class BasePartsQuery extends Query {
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String name;
    @ApiModelProperty(value = "规格")
    private String spec;
    @ApiModelProperty(value = "目前版本号")
    private String version;
    @ApiModelProperty(value = "物料来源")
    private String source;
    @ApiModelProperty(value = "类型")
    private String category;
//    @ApiModelProperty(value = "单重/净重(单位kg)")
//    private Double single;
    @ApiModelProperty(value = "检验否")
    private Boolean isCheck;
    @ApiModelProperty(value = "库存单位")
    private String stockUnit;
//    @ApiModelProperty(value = "安全库存量")
//    private Integer safetyStock;
//    @ApiModelProperty(value = "最高储存数量")
//    private Integer maxStock;
    @ApiModelProperty(value = "主要仓库别")
    private String mainWarehouse;
    @ApiModelProperty(value = "主要储位别")
    private String mainStorage;
    @ApiModelProperty(value = "生产单位")
    private String productionUnit;
//    @ApiModelProperty(value = "生产单位/库存单位换算率")
//    private Integer productionConversionRate;
//    @ApiModelProperty(value = "最少生产数量")
//    private Integer minProductionQty;
//    @ApiModelProperty(value = "生产损耗率")
//    private Double productionLossRate;
    @ApiModelProperty(value = "发料单位")
    private String sentUnit;
//    @ApiModelProperty(value = "发料单位/库存单位换算率")
//    private Integer sentConversionRate;
//    @ApiModelProperty(value = "最少发料数量")
//    private Integer minSentQty;
    @ApiModelProperty(value = "消耗料件否")
    private Boolean isConsume;
//    @ApiModelProperty(value = "储存有效天数")
//    private Integer validityDays;
    @ApiModelProperty(value = "线边主要仓库别")
    private String mainLineWarehouse;
    @ApiModelProperty(value = "线边主要储位别")
    private String mainLineStorage;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "类型")
    private String  typesof;
    @ApiModelProperty(value = "是否为工单")
    private boolean  isom;
    @ApiModelProperty(value = "是否为模板")
    private Boolean  isTemplate;
}
