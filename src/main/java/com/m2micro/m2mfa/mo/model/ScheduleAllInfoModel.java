package com.m2micro.m2mfa.mo.model;

import com.m2micro.m2mfa.mo.entity.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Auther: liaotao
 * @Date: 2019/1/9 17:09
 * @Description:
 */
@Data
@ApiModel(description="排产单所有相关数据")
public class ScheduleAllInfoModel {
    MesMoSchedule mesMoSchedule;
    List<MesMoScheduleShift> mesMoScheduleShifts;
    List<MesMoScheduleStaff> mesMoScheduleStaffs;
    List<MesMoScheduleProcess> mesMoScheduleProcess;
    List<MesMoScheduleStation> mesMoScheduleStation;
}
