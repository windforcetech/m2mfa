package com.m2micro.m2mfa.app.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName WipFail
 * @Description 产品不良原因分布
 * @Author qipeng.xu
 * @Date 2019/6/26 15:35
 * @Version 1.0
 */
@Data
public class WipFail {

    @ApiModelProperty(value = "不良现象代码")
    String defectCode;
    @ApiModelProperty(value = "不良现象名称")
    String defectName;
    @ApiModelProperty(value = "不良现象数量")
    Integer defectNum;

}
