package com.m2micro.m2mfa.pad.model;

import com.m2micro.m2mfa.mo.model.OperationInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/19 11:49
 * @Description:
 */
@ApiModel(description="pad端登陆后工位信息及第一个工位的操作信息")
@Data
public class StationAndOperate {

    private OperationInfo operationInfo;

    private List<PadStationModel> padStationModels;
}
