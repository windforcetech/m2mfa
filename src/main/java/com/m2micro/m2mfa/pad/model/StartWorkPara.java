package com.m2micro.m2mfa.pad.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: chenshuhong
 * @Date: 2019/1/19 15:50
 * @Description:
 */
@Data
@ApiModel(description="pad 下工参数")
public class StartWorkPara {

  @ApiModelProperty(value = "上工记录主键")
  private String rwid;
  @ApiModelProperty(value = "人员作业记录主键")
  private String recordStaffId;

}
