package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.common.validator.AddGroup;
import com.m2micro.m2mfa.common.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


/**
 * @Auther: liaotao
 * @Date: 2019/1/15 17:46
 * @Description:
 */
@ApiModel(description="pad端登陆后工位信息")
@Data
public class PadStationModel {
    @ApiModelProperty(value = "工序主键")
    private String processId;
    @ApiModelProperty(value = "工位主键")
    private String stationId;
    @ApiModelProperty(value = "工位编号")
    private String code;
    @ApiModelProperty(value = "工位名称")
    private String name;
    @ApiModelProperty(value = "工位步骤")
    private Integer step;
}
