package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Auther: liaotao
 * @Date: 2019/2/20 11:09
 * @Description:
 */
@Data
@ApiModel(description="工位作业信息及进度")
@EqualsAndHashCode
public class StationInfoModel {
    @ApiModelProperty(value = "排产单号")
    private String scheduleNo;
    @ApiModelProperty(value = "作业工位")
    private String stationName;
    @ApiModelProperty(value = "作业人数")
    private Integer jobPeoples;
    @ApiModelProperty(value = "提报异常")
    private Boolean abnormalFlag;
    @ApiModelProperty(value = "派工指定作业人员")
    private String peoples;
    @ApiModelProperty(value = "排产数量")
    private Integer scheduleQty;
    @ApiModelProperty(value = "完工数量")
    private Integer completedQty;
    @ApiModelProperty(value = "不良数量")
    private Long qty;
    @ApiModelProperty(value = "报废数量")
    private Integer scrapQty;
    @ApiModelProperty(value = "完成率")
    private String completionRate;
    @ApiModelProperty(value = "不良率")
    private String failRate;
    @ApiModelProperty(value = "报废率")
    private String scrapRate;
}
