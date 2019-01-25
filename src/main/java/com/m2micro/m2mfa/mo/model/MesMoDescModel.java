package com.m2micro.m2mfa.mo.model;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: liaotao
 * @Date: 2018/12/18 17:09
 * @Description:
 */
@ApiModel(description="工单主档综合信息")
@Data
@Builder
public class MesMoDescModel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "工单id")
    private String moId;
    @ApiModelProperty(value = "工单号码")
    private String moNumber;
    @ApiModelProperty(value = "工单类型")
    private String category;
    @ApiModelProperty(value = "料件id")
    private String partId;
    @ApiModelProperty(value = "目标量")
    private Integer targetQty;
    @ApiModelProperty(value = "工单版本")
    private Integer revsion;
    @ApiModelProperty(value = "特性码")
    private String distinguish;
    @ApiModelProperty(value = "母工单")
    private String parentMo;
    @ApiModelProperty(value = "bom版本")
    private Integer bomRevsion;
    @ApiModelProperty(value = "预计投入时间")
    private Date planInputDate;
    @ApiModelProperty(value = "预计完工时间")
    private Date planCloseDate;
    @ApiModelProperty(value = "实际投入时间")
    private Date actualInputDate;
    @ApiModelProperty(value = "实际完成时间")
    private Date actualcLoseDate;
    @ApiModelProperty(value = "生产途程")
    private String routeId;
    @ApiModelProperty(value = "投入工序")
    private String inputProcessId;
    @ApiModelProperty(value = "产出工序")
    private String outputProcessId;
    @ApiModelProperty(value = "达交时间")
    private Date reachDate;
    @ApiModelProperty(value = "预排机台数")
    private Integer machineQty;
    @ApiModelProperty(value = "客户主键")
    private String customerId;
    @ApiModelProperty(value = "订单号码")
    private String orderId;
    @ApiModelProperty(value = "订单项次")
    private Integer orderSeq;
    @ApiModelProperty(value = "完成排产")
    private Integer isSchedul;
    @ApiModelProperty(value = "已排产数量")
    private Integer schedulQty;
    @ApiModelProperty(value = "已投入数量")
    private Integer inputQty;
    @ApiModelProperty(value = "已产出数量")
    private Integer outputQty;
    @ApiModelProperty(value = "报废数量")
    private Integer scrappedQty;
    @ApiModelProperty(value = "不良数量")
    private Integer failQty;
    @ApiModelProperty(value = "状态")
    private Integer closeFlag;
    @ApiModelProperty(value = "有效否")
    private Boolean enabled;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "工单类型名称")
    private String categoryName;
    @ApiModelProperty(value = "料件编号")
    private String partNo;
    @ApiModelProperty(value = "品名")
    private String partName;
    @ApiModelProperty(value = "规格")
    private String partSpec;
    @ApiModelProperty(value = "/涂程名称")
    private String routeName;
    @ApiModelProperty(value = "投入工序名称")
    private String inputProcessName;
    @ApiModelProperty(value = "产出工序名称")
    private String outputProcessName;
    @ApiModelProperty(value = "简称")
    private String customerName;
}
