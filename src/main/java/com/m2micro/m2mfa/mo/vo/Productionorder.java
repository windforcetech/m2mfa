package com.m2micro.m2mfa.mo.vo;


import com.m2micro.m2mfa.mo.entity.MesMoSchedule;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleProcess;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStaff;
import com.m2micro.m2mfa.mo.entity.MesMoScheduleStation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
//排产单数据
public class Productionorder {
    private MesMoSchedule mesMoSchedule;
    private List<MesMoScheduleStaff> mesMoScheduleStaffs;
    private List<MesMoScheduleProcess> mesMoScheduleProcesses;
    private List<MesMoScheduleStation> mesMoScheduleStations;

}
