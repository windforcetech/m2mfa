package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description="作业指导")
@Data
@Builder
public class BasePartInstructionModel extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @ApiModelProperty(value = "id")
  private String id;
  @ApiModelProperty(value = "料件编号")
  private String partNo;
  @ApiModelProperty(value = "料件Id")
  private String partId;
  @ApiModelProperty(value = "品名")
  private String partName;
  @ApiModelProperty(value = "规格")
  private String spec;
  @ApiModelProperty(value = "工序")
  private String processId;
  @ApiModelProperty(value = "工序名称")
  private String processName;
  @ApiModelProperty(value = "工位")
  private String stationId;
  @ApiModelProperty(value = "工位名称")
  private String stationName;
  @ApiModelProperty(value = "指导书编号")
  private String instructionCode;
  @ApiModelProperty(value = "指导书Id")
  private String instructionId;
  @ApiModelProperty(value = "指导书名称")
  private String instructionName;
  @ApiModelProperty(value = "备注")
  private String desription;
  @ApiModelProperty(value = "版本")
  private String revsion;
  @ApiModelProperty(value = "生效日期")
  private Date effectiveDate;
  @ApiModelProperty(value = "失效日期")
  private Date invalidDate;
  @ApiModelProperty(value = "有效否")
  private Boolean enabled;
}
