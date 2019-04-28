package com.m2micro.m2mfa.base.query;

import com.m2micro.framework.commons.util.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Auther: liaotao
 * @Date: 2018/12/3 16:38
 * @Description:
 */
@ApiModel(description = "模具主档参数")
@Data
public class BaseMoldQuery extends Query {

    @ApiModelProperty(value = "模具编号")
    private String code;
    @ApiModelProperty(value = "模具名称")
    private String name;
//    @ApiModelProperty(value = "所属客户")
//    private String customerName;
    @ApiModelProperty(value = "所属客户id")
    private String customerId;
    @ApiModelProperty(value = "模具状态")
    private String flag;
    @ApiModelProperty(value = "模具分类id")
    private String categoryId;
    @ApiModelProperty(value = "固资编号 like")
    private String assdtId;
    @ApiModelProperty(value = "类型")
    private String typeId;
    @ApiModelProperty(value = "摆放位置")
    private String placement;
    @ApiModelProperty(value = "归属部门")
    private String departmentId;
    @ApiModelProperty(value = "对应维修人员")
    private String maintenanceStaff;
    @ApiModelProperty(value = "对应技术人员")
    private String technicalStaff;
    @ApiModelProperty(value = "对应管理人员")
    private String managerStaff;
    @ApiModelProperty(value = "脱模方式")
    private Integer stripp;
    @ApiModelProperty(value = "是否使用模芯")
    private Boolean usemandrel;
    @ApiModelProperty(value = "可用模穴说明")
    private String cavityRemark;
    @ApiModelProperty(value = "开模厂商")
    private String supplierId;
    @ApiModelProperty(value = "模具结构")
    private String moldStructure;
    @ApiModelProperty(value = "材质")
    private String material;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;

}
