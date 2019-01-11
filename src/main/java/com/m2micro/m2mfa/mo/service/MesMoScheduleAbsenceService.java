package com.m2micro.m2mfa.mo.service;

import com.m2micro.m2mfa.mo.vo.Absence;
import com.m2micro.m2mfa.mo.vo.AbsencePersonnel;

import java.util.List;

/**
 * 缺勤service
 */
public interface MesMoScheduleAbsenceService {
    /**
     * 根据职员ID获取缺勤数据
     * @param staffId
     * @return
     */
    List<Absence> mesMoScheduleAbsence(String staffId);

    /**
     * 缺勤存储
     * @param absencePersonnels
     */
    void save(List<AbsencePersonnel>  absencePersonnels);
}
