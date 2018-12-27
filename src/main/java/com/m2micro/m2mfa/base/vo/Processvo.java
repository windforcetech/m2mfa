package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseProcess;
import com.m2micro.m2mfa.base.entity.BaseProcessStation;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class Processvo {
    private BaseProcess baseProcess;
    private List<BaseProcessStation> baseProcessStations;
    private BasePageElemen basePageElemen;

}
