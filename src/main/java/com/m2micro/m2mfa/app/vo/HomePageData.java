package com.m2micro.m2mfa.app.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName HomePageData
 * @Description Mes APP首页数据
 * @Author admin
 * @Date 2019/6/26 11:21
 * @Version 1.0
 */
@Data
public class HomePageData {

    @ApiModelProperty(value = "今日完成百分数")
    private Integer planFinishPercent;

    @ApiModelProperty(value = "运行设备")
    Integer machinerealRun;

    @ApiModelProperty(value = "保养设备")
    Integer machinerealMaintenance;

    @ApiModelProperty(value = "故障设备")
    Integer machinereaMalfunction;

    @ApiModelProperty(value = "停机设备")
    Integer machinereaDowntime;

    @ApiModelProperty(value = "机台产出良品数")
    Integer machineOutput;

    @ApiModelProperty(value = "机台产出不良数")
    Integer machineFailQty;

    @ApiModelProperty(value = "后工序产出良品数")
    Integer bootOutput;

    @ApiModelProperty(value = "后工序产出不良数")
    Integer bootFailQty;

    @ApiModelProperty(value = "产品不良原因分布")
    List<WipFail> wipFailList;



}
