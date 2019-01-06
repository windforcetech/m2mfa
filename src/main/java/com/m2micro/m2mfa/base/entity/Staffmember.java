package com.m2micro.m2mfa.base.entity;

import com.m2micro.framework.starter.entity.Organization;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class Staffmember {
    private BaseShift shift;
    private List<BaseStaff> staffs;
    private List<BaseStation> stations;
    private List<Organization> departments;
}
