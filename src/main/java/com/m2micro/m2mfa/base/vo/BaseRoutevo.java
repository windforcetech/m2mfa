package com.m2micro.m2mfa.base.vo;

import com.m2micro.m2mfa.base.entity.BasePageElemen;
import com.m2micro.m2mfa.base.entity.BaseRouteDef;
import com.m2micro.m2mfa.base.entity.BaseRouteDesc;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseRoutevo {
    private BaseRouteDesc baseRouteDesc;
    private BaseRouteDef  baseRouteDef;
    private BasePageElemen basePageElemen;
}
