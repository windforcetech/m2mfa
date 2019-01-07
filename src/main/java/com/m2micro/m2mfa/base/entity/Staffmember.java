package com.m2micro.m2mfa.base.entity;

import com.m2micro.m2mfa.pr.vo.OrganizationalStation;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class Staffmember {

    private BaseProcess baseProcess;
    private BaseShift shift;
    private List<BaseStaff> staffs;
    private List<OrganizationalStation> organizationalStations;
}
