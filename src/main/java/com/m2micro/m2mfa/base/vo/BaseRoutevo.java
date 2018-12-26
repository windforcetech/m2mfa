package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BaseRoutevo {
    private BaseRouteDesc baseRouteDesc;
    private List<BaseRouteDef> baseRouteDefs;
    private BasePageElemen basePageElemen;
}
