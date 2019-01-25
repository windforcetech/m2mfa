package com.m2micro.m2mfa.pr.vo;

import com.m2micro.m2mfa.pr.entity.MesPartRoute;
import com.m2micro.m2mfa.pr.entity.MesPartRouteProcess;
import com.m2micro.m2mfa.pr.entity.MesPartRouteStation;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MesPartvo {
    private MesPartRoute mesPartRoute;
    private List<MesPartRouteProcess> mesPartRouteProcesss;
    private List<MesPartRouteStation> mesPartRouteStations;
    private BigDecimal scheduleTime;

}
