package com.m2micro.m2mfa.mo.vo;

import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SchedulerDistribution {
    private List<MesMoScheduleStaff> mesMoScheduleStaffs;
    private List<MesMoScheduleStation> mesMoScheduleStations;
}
