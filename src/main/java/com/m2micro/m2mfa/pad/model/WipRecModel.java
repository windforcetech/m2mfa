package com.m2micro.m2mfa.pad.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "WipRecModel", description = "在制信息")
public class WipRecModel {
    @ApiModelProperty(value = "序号(批号)")
    private String serialNumber;
    @ApiModelProperty(value = "产出数(数量)")
    private Integer outputQty;
    @ApiModelProperty(value = "出工序时间(出站时间)")
    private Date outTime;
}
